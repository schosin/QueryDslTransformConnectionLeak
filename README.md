# QueryDslTransformConnectionLeakApplication

Simple reproducible example project showcasing a connection leak caused by `FetchableQueryBase#transform` when used with JPA/Hibernate in a Spring Boot application.

Run the tests to see the connection leak when calling `FetchableQueryBase#transform` without an active transaction.

