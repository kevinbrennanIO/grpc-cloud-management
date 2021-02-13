# grpc-cloud-management

**National College of Ireland\
BSC (Honours) in Computing\
Distributed Systems\
Project 2020-21**

**Student No:** x16149823@student.ncirl.ie\
**Student Name:** Kevin Brennan

## Simulate Management of a Cloud Environment

### Project Aims

The purpose of this project is to gain experience in designing and building distributed systems.

### Project Deliverables

#### Week 6

- A short project proposal must be first defined detailing the description of your application, domain and the three (3) services.

#### Week 9

- A report which details the scenario and services you have chosen. Additionally, this should specify the message formats for data exchange and service actuation. The report must have all the headings of the marking scheme, such as

  - Service Definitions
  - Service Implementations
  - Naming Services
  - Remote Error Handling
  - Client
  - Graphical User Interface
  - GitHub

- Code Submission

  - Available on GitHub
  - Well commented
  - Regular commits

- Video Submission

  - Duration 10mins
  - Recorded

## Marking Scheme

- **Proposal** (5%)

- **Report** (5%)

- **Service Definitions** - use of gRPC proto (20%)
  - For each of the 3 different services/devices a corresponding proto file is defined and used (3 services * 2 = 6%)
  - All 4 different types of RPC invocation styles have been used (simple RPC, server-side streaming RPC, client-side streaming RPC, bidirectional streamingRPC) (4 styles * 2 = 8%)

- **Service Implementations** - 3 sufficiently complex service implementations (35%)
  - One service must be written in a language other than Java (10%)
  - The other two services should be written in Java. (2 services * 8 =16%)
  - Service complexity and implementation (3 services * 3 =9%)

- **Naming Service** - use of jmDNS (12%)
  - Marks for registration (3 services * 2 = 6%)
  - Marks for discovery (3 services * 2 = 6%)
  - Appropriate and different message structures and field types are used (6%)

- **Remote Error Handling** (5%)

- **Client - Graphical User Interface** (14%)
  - That allows to view (e.g., present, discover), control (parameters) and invoke the services/devices. That is the also the client for each of the 3 services (3 services * 3 = 9%)
  - The GUI can be developed in any language, technology of choice (Java application, web based,etc) (5%)

- **GitHub** (4%)
  - Maintain a repo with a regular commit history
