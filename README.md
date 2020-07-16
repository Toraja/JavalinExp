# Inquiry
Web app for experimenting Javalin and J2html, as well as Testcontainer

## Features
### Submit inquiries
- [x] Submit your inquiry.
- [ ] When inquiry is submitted, a staff is assigned.

### View inquiries
- [x] View all inquiries with pagenation.
- [x] View each inquiry with detail.

### Mark inquiry solved
- [ ] Mark selected inquiry solved.

## Configuration
### Logging
Modify `simplelogger.properties` under resources directory.

## Note
### Dockerfile
`Dockerfile` is dockerfile for DB and it should be named as `db.Dockerfile`.
However, due to the restriction of Testcontainers, it is named as `Dockerfile`.  
This asymmetry can be solved by separating directory of APP and DB, but left as
is for simplicity.  
(FYI: Symbolic link somehow does not work)
