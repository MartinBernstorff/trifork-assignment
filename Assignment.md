# Introduction

In this assignment we are looking at data for healthcare organizations.

The data is sourced from Sundhedsvæsenets Organisationsregister (SOR) (Healthcare Organization Registry), which provides
the data in CSV
format ([`sor.csv`](https://sor-filer.sundhedsdata.dk/sor_produktion/data/sor/sorcsv/V_1_2_1_19/sor.csv)).

### Data in the csv file

The CSV file contains a lot of information about the organizations.
The relevant data is:

| Property        | CSV Header name   | Description                                                                   |
| --------------- | ----------------- | ----------------------------------------------------------------------------- |
| Name            | `Enhedsnavn`      | Name of the organization                                                      |
| Type            | `Enhedstype`      | Type of organization, e. g. private ("privat"), municipality ("kommune") etc. |
| Region          | `P_Region`        | Region                                                                        |
| Specialty       | `Hovedspeciale`   | Main specialty                                                                |
| SOR code        | `SOR-kode`        | SOR-code                                                                      |
| Parent SOR code | `Parent-SOR-kode` | SOR-code of parent organization                                               |
| CVR             | `CVR`             | CVR-number                                                                    |

The organizations are organized in a hierarchy, like this:

```
- Organization A
  - Child Org A1
    - Child Org A1.1
      - Child Org A1.1.1
    - Child Org A1.2
  - Child Org A2
- Organization B
  ...
```

In this example "Child Org A1" has parent "Organization A" and immediate children "Child Org A1.1" and "Child Org A1.2".

"Organization A" is the "top level parent" of all organizations below it in the hierarchy.

# Assignment

In this assignment you are tasked with building a small application in Java using Spring Boot.
We recommend initializing the project using https://start.spring.io/.

The application should provide data for healthcare organizations via an API.

The API must include 2 endpoints:

## Lookup Endpoint

This endpoint allows for looking up organizations by SOR-code, which is the unique identifier.
It takes the SOR-code as input parameter, and the response contains the following information:

- Name
- Type
- Region
- SOR-code
- CVR
- reference/link/SOR code of parent organization (if any)
- reference/link/SOR code of all immediate child organizations (if any)

## Traverse Endpoint

This endpoint allows for traversing the hierarchy of organizations upwards, finding the top level parent organization for any given organization.
This endpoint takes a SOR code as input parameter, and the response contains the top level parent organization in that hierarchy.
