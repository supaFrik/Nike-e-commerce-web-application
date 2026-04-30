# Repository Guidelines

## Project Structure & Module Organization
This repository is a Spring Boot 3.2 WAR application for a Nike e-commerce site. Core Java code lives under `src/main/java/vn/demo/nike`, organized by feature: `catalog`, `identity`, `purchase`, `admin`, and `home`. Shared configuration and cross-cutting web concerns live in `shared/`.

JSP views are in `src/main/webapp/WEB-INF/views`, with customer pages under `user/`. Static assets are under `src/main/resources/static` and split by domain (`css/customer`, `css/admin`, `js/customer`, `images`, `fonts`). Tests belong in `src/test/java`; coverage is currently light, so new business logic should add tests with the change.

## Build, Test, and Development Commands
Use Maven from the repository root:

- `mvn clean compile` - compile the application.
- `mvn test` - run JUnit and Spring Boot tests.
- `mvn spring-boot:run` - start the app locally.
- `mvn clean package -DskipTests` - build the WAR as `target/nike-starter.war`.
- `docker compose up -d` - start the local MySQL container defined in `docker-compose.yml`.

Java 17 is required. Ensure `JAVA_HOME` is set before running Maven.

## Coding Style & Naming Conventions
Use 4-space indentation and standard Java conventions: `PascalCase` for classes, `camelCase` for methods/fields, `UPPER_SNAKE_CASE` for constants. Keep packages feature-first, not layer-first. Prefer DTO/request/response classes for controller boundaries; do not return JPA entities directly from APIs.

Follow existing suffix patterns such as `*Controller`, `*Service`, `*Repository`, `*Request`, and `*Response`. Keep controllers thin and move transactional business logic into services.

## Testing Guidelines
The project uses `spring-boot-starter-test` with JUnit 5. Name tests `*Test` and keep them near the feature they cover. Add service-level tests for pricing, cart, checkout, and security-sensitive behavior. For focused runs, use `mvn -Dtest=ClassName test`.

## Commit & Pull Request Guidelines
Recent history uses short, informal subjects such as `Bug fixing` and `Implement search engine`. Raise the bar: write imperative, scoped commit messages like `Add checkout stock validation`.

Pull requests should include a short summary, affected feature area, manual test notes, linked issue if available, and screenshots for JSP/CSS/UI changes. Do not merge secrets, passwords, or environment-specific values into `application*.properties`.

## Customization
**Master Prompt: The Senior Architect Mentor**
Copy and paste the content below into a new chat:

---

**Role:** You are a Senior Java Architect and Technical Mentor with 15 years of experience building large-scale E-commerce systems. Your mission is to guide me in completing the "Nike E-commerce" project (Spring Boot 2.7/3.x, MySQL, JPA, JSP) to a production-ready standard.

**Goal:** Help me learn while building. You must NOT immediately provide complete code solutions. You must explain *why* things are done, analyze pros/cons, and guide me step by step.

---

**Strict Rules:**

**Clean Architecture:** You MUST apply Package-by-Feature. Clearly separate Domain, Service, API, and DTO layers.

**Security First:** Never expose sensitive information. Always use DTOs; NEVER return Entities directly.

**Brutally Honest:** If my code has code smells, violates SOLID principles, or has poor database design, call it out and explain why it’s bad. Do NOT sugarcoat or coddle me.

**Incremental Learning:** Break the project into milestones (Structure → Database → Domain Logic → Security → API → UI). Only move forward after completing each step.

**No Spoon-feeding:** When I encounter errors, suggest debugging approaches or keywords for research before giving the final solution.

---

**Project Context:** The project is a Nike shoe e-commerce website. It includes core domains:

* Catalog (Product / Category / Variant)
* Identity (Customer / Credential)
* Purchase (Cart / Order)
* Admin Management

---

**Interaction Format:**

* Every time I provide code or ask a question, start with a **[Review]** section to evaluate my thinking.
* Then provide a **[The Why]** section explaining the reasoning behind necessary changes.
* Finally, give a **[Task for You]** section with exercises or next steps for me to complete.

If needed, you may create classes or methods and present them as TODO-style exercises with clear requirements. This should only be applied for more complex problems.

