# Milk Collection — Zenalyst AI Assignment

Backend for a dairy that collects milk twice a day from 1,400 farmers across
60 villages using 22 tankers. Solves four problems from the brief:
stale/static routes, spoilage caused by delay, shared collection points, and
zero visibility into where a tanker currently is.

#Tech Stack

- Java 17, Spring Boot 4.1.1
- Spring Data JPA (Hibernate)
- MySQL
- Maven

## Prerequisites

Before running this project, make sure you have:

1. **Java 17** (JDK) installed
     Check with: 'java -version'
     Download: https://adoptium.net (Eclipse Temurin/SpringToolsForEclipse/IntelliJ IDE)

2. **Maven** installed
     Check with: 'mvn -version'
     Download: https://maven.apache.org/download.cgi
     If using an IDE like Spring Tool Suite or IntelliJ, Maven usually comes bundled — a separate install may not be needed

3. **MySQL Server** installed and running
     Download: https://dev.mysql.com/downloads/mysql/
     During installation, set a root password and remember it
     MySQL Workbench (a GUI tool) is optional but recommended for inspecting
     the database visually — bundled with the MySQL installer, or download
     separately: https://dev.mysql.com/downloads/workbench/

# How to Run

1. Create the database (tables are auto-created by Hibernate, you only need the empty schema):
	'''sql
   	   CREATE DATABASE dairydb;
	'''
2. Update 'src/main/resources/application.yml' with your MySQL username/password if different from defaults.
3. From the project root:
	'''bash
  	   mvn spring-boot:run
	'''
4. On first startup, seed data is generated automatically: 60 villages, around 150
   collection points, 22 tankers, 1 chilling plant, and 1,400 farmers.
   Subsequent restarts skip reseeding automatically.
5. API is available at 'http://localhost:8080'.

# Architecture: Planning Side vs Running Side

- **Planning side** ('service/planning') — generates route templates: which
  tanker covers which collection points, and in what order. Runs
  occasionally (on demand), not tied to a specific day.
- **Running side** ('service/operations') — tracks the actual twice-daily
  execution of a route: starting a run, recording real check-in timestamps
  at each stop, plant delivery, and spoilage evaluation. This was the primary
  focus of this build, since it most directly solves the brief's core it solves the complaint 'farmers having no way to know where their tanker is'.

# Key API Endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| POST | '/routes/generate?runType=MORNING' | Generate routes for all tankers |
| POST | '/runs/start?routeId=1&runDate=2026-09-09' | Start today's execution of a route |
| POST | '/runs/stops/{id}/arrive' | Driver check-in: arrived at a stop |
| POST | '/runs/stops/{id}/depart' | Driver check-in: departed a stop |
| POST | '/runs/stops/{id}/skip' | Mark a stop skipped |
| POST | '/runs/{id}/deliver?chillingPlantId=1' | Record plant delivery |
| GET | '/runs/{id}/status' | Where is this tanker |
| GET | '/runs/{id}/pickup-status?collectionPointId=X' | Has my milk been picked up |
| POST | '/farmers' | Add a new farmer |
| POST | '/collection-points' | Add a new collection point |
| POST | '/runs/{id}/complete' | Mark a run completed |

# Assumptions

- **Spoilage threshold**: 180 minutes (3 hours) from pickup to plant
  delivery, configurable via 'dairy.spoilage-threshold-minutes' in
  'application.yml'. Not specified in the brief, so a reasonable dairy
  industry figure was assumed.
- **Spoilage is judged per stop, not per whole load** — each farmer's milk is
  evaluated against its own actual pickup-to-delivery elapsed time, even
  though physically all milk in a tanker is mixed together. This gives more
  precise, farmer-level reporting.
- **No real GPS** — tanker location/status is derived from manual driver
  check-ins at each stop (arrive/depart), not continuous coordinates. 
- **Simplified distance/travel time** — collection points use simplified
  lat/lng coordinates and a flat-earth distance approximation with an 
  assumed average tanker speed of 30 km/h. Reasonable for relative
  route ordering at this scale and not suitable for real-world navigation.
- **Route planning algorithm** — clusters collection points evenly across
  tankers, then orders each tanker's cluster using nearest-neighbor. This is
  a deliberately simple heuristic, not a real Vehicle Routing Problem (VRP)
  solver. 
- **Farmer/collection-point mapping is treated as input data** —The planning algorithm only decides
  tanker-to-point assignment and stop sequencing, not which farmers use
  which point.
- **Collection point sharing** — modeled via a plain foreign key from Farmer
  to CollectionsPoint and  multiple farmers can point to the same collection point.
- **One chilling plant** — the brief refers to "the chilling plant" 
  (singular), so this was modeled as a single plant rather than assumed. 
  The schema still supports multiple 'ChillingPlant' records for 
  extensibility, but only one is seeded and used.

# What Was Left Out (and Why)

- **Real VRP-based route optimization** — Due to time constraint it was not included.
- **Tanker capacity constraints during planning** — 'Tanker.capacityLiters'
  and 'Farmer.avgMilkQtyLiters' exist in the schema, but the planner doesn't
  yet enforce capacity limits when assigning stops. Left out for time.
- **Authentication/authorization** — no login, no role separation like
  driver/dispatcher/admin.
- **Real GPS/live map UI** — deliberately replaced with check-in-based
  status tracking (see Assumptions above).
- **Exception logging** (breakdowns, roadblocks) — considered but
  intentionally left out of this build to focus time on the core
  planning/running/spoilage/status flow. 
- **Farmer/collection-point full lifecycle management** — only basic
  creation endpoints exist ('POST /farmers', 'POST /collections-points'), to
  prove the system adapts to new data. Full CRUD is not implememted.
- **SMS/notification to farmers** — status is available via API/query
  ('GET /runs/{id}/pickup-status'), but no push notification system to send SMS was
  built.
- **Automated tests** — manual testing was done via Postman throughout
  development, formal unit/integration tests were not written given the
  time constraint.

# Data Model Overview

Village → CollectionPoint → Farmer (many farmers can share one point)
Tanker → Route → RouteStop (the planned template)
Route → Run → RunStop (a specific day's actual execution, with real timestamps)
Run → PlantDelivery → SpoilageResult (computed once delivery happens)

'Route'/'RouteStop' represent the reusable plan; 
'Run'/'RunStop' represent one day's actual instance of executing that plan — this separation is what
allows the same route to run every morning and evening without conflicts, while preserving full history of each execution.