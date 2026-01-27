# 统一人证核验系统 - 证件类型配置化实现

## Context

### Original Request

构建统一证件验证中台系统，实现证件类型配置化、应用范围配置化和统一验证规则，解决以下问题：

1. **证件类型配置化**: 每次新增证件类型都需要逐个系统逐个界面增加，维护成本高
2. **应用范围配置化**: 部分证件开放范围有限，需要代码级改造才能调整
3. **统一验证规则**: 省内所有系统同一业务的证件校验规则不统一，维护困难

### Interview Summary

**Key Discussions**:
- MVP范围确认：支持全部15种证件类型（完整实现，非分阶段），暂不集成硬件读卡器，使用表单输入，人证比对预留接口
- 技术栈确认：后端Spring Boot 3.3.2 + JDK 17，前端Vue 3.x + Element Plus，数据库OceanBase（兼容MySQL协议）
- 外部数据集成：实时拉取模式，每次验证时通过配置的外部API地址获取办理通路、渠道小类、地市、区县、机构信息
- 数据库设计：全新设计4张核心表（certificate_type、certificate_type_field、validation_rule、scope_config）
- 接口设计：按RESTful最佳实践自由设计，JSON格式，API Key认证
- 应用范围配置：5层结构（办理通路→渠道小类→地市→区县→机构），最末级配置优先
- 测试策略：单元测试+集成测试+端到端测试，覆盖率要求80%以上

**Research Findings**:
- 后端最佳实践：使用Spring Validator实现动态验证，Spring Cache缓存规则，数据库存储配置表，封装统一验证服务
- 前端最佳实践：Element Plus动态表单渲染，将后端规则转换为Element Plus规则格式，自定义验证器处理复杂逻辑

### Metis Review

**Status**: Skipped due to technical issues with agent invocation. Gaps identified through self-review instead.

**Identified Gaps (addressed)**:
- Gap: External API response format not specified → Resolved: Use JSON format, handle errors gracefully
- Gap: Cache invalidation strategy not defined → Resolved: Use time-based TTL (e.g., 30 minutes) for external data cache
- Gap: Batch import format details not specified → Resolved: Use Excel template with predefined columns
- Gap: Conflict resolution strategy for scope config → Resolved: Explicitly document the priority rule (lowest level wins)

---

## Work Objectives

### Core Objective

构建一个可配置的统一证件验证中台系统，支持15种证件类型的动态配置、应用范围配置化和统一验证规则管理，解决证件类型和应用范围需要代码级改造的问题。

### Concrete Deliverables

- **后端服务**（Spring Boot 3.3.2）:
  - 证件类型配置管理API（CRUD）
  - 应用范围配置管理API（CRUD + 批量导入）
  - 统一验证接口（支持动态规则验证）
  - 数据库表结构（4张核心表）

- **前端应用**（Vue 3 + Element Plus）:
  - 证件类型配置界面
  - 应用范围配置界面（单个+批量导入）
  - 统一验证测试界面

- **验证规则实现**:
  - 15种证件类型的验证规则
  - 动态规则加载和执行

### Definition of Done

- [ ] 所有15种证件类型的配置数据已初始化
- [ ] 后端所有API接口已实现并通过单元测试
- [ ] 前端所有管理界面已实现并通过集成测试
- [ ] 统一验证接口已实现并通过端到端测试
- [ ] 单元测试覆盖率达到80%以上
- [ ] 所有接口文档已编写完成
- [ ] 系统可通过API Key正常调用验证接口

### Must Have

- 15种证件类型的完整配置（包括字段、验证规则、照片要求）
- 5层应用范围配置结构（办理通路→渠道小类→地市→区县→机构）
- 统一验证接口支持动态规则验证
- 批量导入功能（Excel格式）
- API Key认证机制
- 基础CRUD管理界面

### Must NOT Have (Guardrails)

- **硬件读卡器集成**: MVP阶段不实现硬件读卡器功能
- **人证比对实现**: 仅预留接口，不实现具体比对逻辑
- **全局渠道配置**: 不包含全网渠道（如京东OAO）的配置
- **前端验证规则硬编码**: 所有验证规则必须从后端动态加载，禁止前端硬编码
- **同步外部数据**: 不实现外部数据同步功能，使用实时拉取模式
- **复杂权限管理**: MVP阶段仅实现API Key认证，不实现RBAC等复杂权限

---

## Verification Strategy (MANDATORY)

### Test Decision

- **Infrastructure exists**: NO（全新项目）
- **User wants tests**: YES（TDD模式）
- **Framework**:
  - 后端: JUnit 5 + Mockito
  - 前端: Vitest

### TDD Workflow

Each TODO follows RED-GREEN-REFACTOR:

**Task Structure**:
1. **RED**: Write failing test first
   - Test file: `[path].java` / `[path].spec.ts`
   - Test command: `mvn test` / `npm run test`
   - Expected: FAIL (test exists, implementation doesn't)

2. **GREEN**: Implement minimum code to pass
   - Command: `mvn test` / `npm run test`
   - Expected: PASS

3. **REFACTOR**: Clean up while keeping green
   - Command: `mvn test` / `npm run test`
   - Expected: PASS (still)

**Test Setup Task**:
- [ ] 0. Setup Test Infrastructure
  - 后端: 确认Spring Boot starter test已引入（通常包含JUnit 5, Mockito）
  - 前端: 安装Vitest `npm install -D vitest @vue/test-utils`，配置vitest.config.ts
  - Verify: `mvn test` → shows help, `npm run test` → 0 tests
  - Example: 创建 `src/test/java/example/ExampleTest.java` 和 `src/__tests__/example.spec.ts`
  - Verify: `mvn test` → 1 test passes, `npm run test` → 1 test passes

---

## Task Flow

 ```
 Task 1 (后端项目初始化)
   → Task 2-4 (数据库设计 + 实体类开发) [并行]
       → Task 5-7 (Mapper层 + Service层 + API层) [并行]
           → Task 8 (统一验证服务)
               → Task 9 (外部数据集成)
                   → Task 10 (后端单元测试)

 Task 11 (前端项目初始化)
   → Task 12-13 (页面组件 + API封装) [并行]
       → Task 14 (动态表单渲染)
           → Task 15 (前端测试)

 Task 16 (端到端测试)
   → Task 17 (文档编写)
 ```

## Parallelization

| Group | Tasks | Reason |
|-------|-------|--------|
| A | 2, 3, 4 | 独立的数据库表和实体类 |
| B | 5, 6, 7 | 独立的Mapper、Service、API层 |
| C | 12, 13 | 独立的页面组件和API封装 |

| Task | Depends On | Reason |
|------|------------|--------|
| 5 | 2, 3, 4 | Mapper依赖实体类 |
| 6 | 5 | Service依赖Mapper |
| 7 | 6 | API依赖Service |
| 8 | 6 | 统一验证服务依赖Service |
| 9 | 8 | 外部数据集成依赖验证服务 |
| 10 | 7, 8, 9 | 测试依赖所有实现 |
| 14 | 12, 13 | 动态表单依赖组件和API |
| 15 | 12, 13, 14 | 测试依赖所有实现 |
| 16 | 10, 15 | E2E测试依赖后端和前端 |

---

## TODOs

### Phase 1: 后端项目初始化

- [ ] 1. 初始化Spring Boot 3.3.2后端项目

   **What to do**:
   - 使用Spring Initializr创建Spring Boot 3.3.2项目
   - 添加依赖: Spring Web, MyBatis Plus Boot Starter, Spring Cache, H2 Database（开发环境）, MySQL Driver（OceanBase兼容）, Lombok
   - 配置application.yml:
     - 数据库连接配置（H2 for dev, OceanBase for prod）
     - MyBatis Plus配置（mapper扫描路径、类型别名包）
     - Spring Cache配置
     - 外部API配置（办理通路、渠道小类、地市、区县、机构的API地址）
     - 日志配置
   - 创建基础包结构: com.newland.ucms.certificate.{{controller, service, mapper, entity, config, dto, exception}}

  **Must NOT do**:
  - 不要添加不必要的依赖
  - 不要配置复杂的日志框架（使用默认的SLF4J）

  **Parallelizable**: NO (must start first)

  **References** (CRITICAL - Be Exhaustive):

  **Pattern References** (existing code to follow):
  - 无（全新项目）

  **API/Type References** (contracts to implement against):
  - 无（全新项目）

  **Test References** (testing patterns to follow):
  - 无（全新项目）

   **Documentation References** (specs and requirements):
   - AGENTS.md: Spring Boot 3.3.2, JDK 17, REST API规范
   - 需求.md: 证件类型配置、应用范围配置、统一验证规则
   - CHBN融合业务支撑项目_统一证件管理平台_产品需求说明书_0126.md：产需文档

   **External References** (libraries and frameworks):
   - Spring Boot官方文档: https://spring.io/projects/spring-boot
   - MyBatis Plus文档: https://baomidou.com/
   - OceanBase文档: https://www.oceanbase.com/docs/

   **WHY Each Reference Matters** (explain the relevance):
   - Spring Boot官方文档: 了解项目初始化、依赖管理、配置最佳实践
   - MyBatis Plus文档: 了解BaseMapper的使用、CRUD操作、条件构造器
   - OceanBase文档: 了解数据库连接配置、SQL兼容性

  **Acceptance Criteria**:

  **TDD**:
  - [ ] Test file created: `src/test/java/com/ucms/certificate/ProjectInitializationTest.java`
  - [ ] Test covers: Spring Boot应用启动成功，数据库连接成功
  - [ ] `mvn test` → PASS (2 tests, 0 failures)

  **Manual Execution Verification**:
  - [ ] Run: `mvn spring-boot:run`
  - [ ] Verify: 控制台输出"Started CertificateApplication in X seconds"
  - [ ] Verify: 数据库连接成功，无错误日志

  **Evidence Required**:
  - [ ] 控制台启动日志截图
  - [ ] 测试通过日志截图

  **Commit**: YES
  - Message: `feat(backend): 初始化Spring Boot项目`
  - Files: pom.xml, src/main/resources/application.yml, src/main/java/com/ucms/certificate/CertificateApplication.java
  - Pre-commit: `mvn test`

### Phase 2: 数据库设计
表名前缀:UCMS_
- [ ] 2. 设计并创建数据库表结构

   **What to do**:
   - 创建SQL脚本文件 `src/main/resources/db/schema.sql`:
     - `UCMS_CERTIFICATE_TYPE` 表（证件类型主表）:
       - id (BIGINT PK, AUTO_INCREMENT)
       - cert_type_code (VARCHAR(10), UNIQUE, 证件类型代码，如1, 2, 3等)
       - cert_type_name (VARCHAR(50), 证件类型名称，如身份证、护照等)
       - status (TINYINT, 状态：0-停用, 1-启用)
       - created_at (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP)
       - updated_at (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)
       - created_by (VARCHAR(50), 创建人)
       - updated_by (VARCHAR(50), 修改人)
       - is_deleted (TINYINT, DEFAULT 0)

     - `UCMS_PUBLIC_FIELD_LIBRARY` 表（公共字段库表）:
       - id (BIGINT PK, AUTO_INCREMENT)
       - field_name (VARCHAR(50), 字段名称，如姓名、民族、性别等)
       - field_name_en (VARCHAR(50), 英文字段名称，如name、nation、gender等)
       - created_at (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP)
       - is_deleted (TINYINT, DEFAULT 0)

     - `UCMS_CERTIFICATE_TYPE_FIELD` 表（证件类型字段配置表）:
       - id (BIGINT PK, AUTO_INCREMENT)
       - cert_type_code (VARCHAR(10), FK UCMS_CERTIFICATE_TYPE.cert_type_code)
       - field_name (VARCHAR(50), 字段名称，如name、cert_number等)
       - field_display_name (VARCHAR(50), 字段显示名称，如姓名、证件号码等)
       - is_required (TINYINT, 是否必填：0-否, 1-是)
       - field_type (VARCHAR(20), 字段类型：text/number/date等)
       - field_length (INT, 字段长度)
       - sort_order (INT, 排序)
       - created_at (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP)
       - is_deleted (TINYINT, DEFAULT 0)

     - `UCMS_VALIDATION_RULE_CONFIG` 表（校验规则配置表）:
       - id (BIGINT PK, AUTO_INCREMENT)
       - business_type (VARCHAR(100), 业务类型，如"档案补正"等)
       - business_code (VARCHAR(50), 业务编号)
       - channel_sub_type (VARCHAR(50), 受理渠道)
       - allowed_cert_types (TEXT, 可用证件类型，JSON数组)
       - status (TINYINT, 状态：0-停用, 1-启用)
       - created_at (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP)
       - updated_at (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)
       - created_by (VARCHAR(50), 创建人)
       - updated_by (VARCHAR(50), 修改人)
       - is_deleted (TINYINT, DEFAULT 0)

     - `UCMS_VALIDATION_RULE_ITEM` 表（校验规则明细表）:
       - id (BIGINT PK, AUTO_INCREMENT)
       - rule_id (BIGINT, FK UCMS_VALIDATION_RULE_CONFIG.id)
       - field_name (VARCHAR(50), 字段名称，关联证件类型字段)
       - validation_logic (VARCHAR(50), 校验逻辑，如">"、"<"、">="等)
       - validation_value (VARCHAR(100), 校验值，如"18"表示age>18)
       - sort_order (INT, 排序)
       - created_at (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP)
       - is_deleted (TINYINT, DEFAULT 0)

     - `UCMS_SCOPE_CONFIG` 表（应用范围配置表）:
       - id (BIGINT PK, AUTO_INCREMENT)
       - business_channel (VARCHAR(50), 应用系统，如移动云厅、网格通等，可为空表示全局)
       - channel_sub_type (VARCHAR(50), 渠道小类，可为空)
       - city (VARCHAR(50), 地市，可为空)
       - district (VARCHAR(50), 区县，可为空)
       - institution (VARCHAR(100), 机构，可为空)
       - allowed_cert_types (TEXT, 允许的证件类型列表，JSON数组，如["1","2","3"]，支持多个，使用"|"分隔)
       - status (TINYINT, 状态：0-停用, 1-启用)
       - created_at (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP)
       - updated_at (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)
       - created_by (VARCHAR(50), 创建人)
       - updated_by (VARCHAR(50), 修改人)
       - is_deleted (TINYINT, DEFAULT 0)

   - 添加索引:
     - UCMS_CERTIFICATE_TYPE: idx_cert_type_code, idx_status, idx_is_deleted
     - UCMS_PUBLIC_FIELD_LIBRARY: idx_field_name, idx_is_deleted
     - UCMS_CERTIFICATE_TYPE_FIELD: idx_cert_type_code, idx_is_deleted
     - UCMS_VALIDATION_RULE_CONFIG: idx_business_code, idx_channel_sub_type, idx_status, idx_is_deleted
     - UCMS_VALIDATION_RULE_ITEM: idx_rule_id, idx_field_name, idx_is_deleted
     - UCMS_SCOPE_CONFIG: idx_business_channel, idx_channel_sub_type, idx_city, idx_district, idx_institution, idx_status, idx_is_deleted

   - 创建初始化数据脚本 `src/main/resources/db/data.sql`:
     - 插入公共字段库数据（姓名、民族、性别、证件号码、期限起始、期限终止、出生日期、签发机关、地址）
     - 插入15种证件类型的配置数据（根据产需文档的证件类型配置需求）

  **Must NOT do**:
  - 不要使用ENUM类型（使用VARCHAR保持灵活性）
  - 不要添加不必要的字段
  - 不要使用触发器（使用应用层逻辑）

  **Parallelizable**: NO (with 3, 4)

  **References** (CRITICAL - Be Exhaustive):

  **Pattern References** (existing code to follow):
  - 无（全新项目）

  **API/Type References** (contracts to implement against):
  - 无（全新项目）

  **Test References** (testing patterns to follow):
  - 无（全新项目）

   **Documentation References** (specs and requirements):
   - CHBN融合业务支撑项目_统一证件管理平台_产品需求说明书_0126.md: 证件类型配置、校验规则配置、应用范围配置的详细需求
   - 公共字段列表：姓名、民族、性别、证件号码、期限起始、期限终止、出生日期、签发机关、地址
   - 字段来源：公共字段（固定选项）+ 自定义字段（动态添加）

  **External References** (libraries and frameworks):
  - OceanBase SQL文档: https://www.oceanbase.com/docs/community-observer-cn-10000000000903252
  - MySQL SQL参考: https://dev.mysql.com/doc/refman/8.0/en/

  **WHY Each Reference Matters** (explain the relevance):
  - 需求.md: 了解15种证件类型的具体字段、采集方式、照片要求等详细信息
  - OceanBase SQL文档: 确保SQL语法与OceanBase兼容
  - MySQL SQL参考: OceanBase兼容MySQL，可参考MySQL文档

  **Acceptance Criteria**:

  **TDD**:
  - [ ] Test file created: `src/test/java/com/ucms/certificate/repository/CertificateTypeMapperTest.java`
  - [ ] Test covers: 数据库表创建成功，初始化数据插入成功
  - [ ] `mvn test` → PASS (3 tests, 0 failures)

  **Manual Execution Verification**:
  - [ ] Run: `mysql -h <host> -u <user> -p <database> < src/main/resources/db/schema.sql`
  - [ ] Verify: 表创建成功，无错误信息
  - [ ] Run: `mysql -h <host> -u <user> -p <database> < src/main/resources/db/data.sql`
  - [ ] Verify: 数据插入成功，使用 `SELECT COUNT(*) FROM UCMS_CERTIFICATE_TYPE` 验证15条记录

  **Evidence Required**:
  - [ ] SQL脚本执行成功的日志截图
  - [ ] 数据库表结构截图（DESCRIBE UCMS_CERTIFICATE_TYPE等）
  - [ ] 数据查询截图（SELECT * FROM UCMS_CERTIFICATE_TYPE等）

  **Commit**: YES
  - Message: `feat(db): 设计并创建数据库表结构`
  - Files: src/main/resources/db/schema.sql, src/main/resources/db/data.sql
  - Pre-commit: `mvn test`

- [ ] 3. 创建实体类和DTO

   **What to do**:
   - 创建实体类（使用MyBatis Plus注解）:
     - `CertificateType.java`: @TableName("UCMS_CERTIFICATE_TYPE"), @TableId(type=IdType.AUTO), Lombok注解
     - `PublicFieldLibrary.java`: @TableName("UCMS_PUBLIC_FIELD_LIBRARY"), @TableId(type=IdType.AUTO), Lombok注解
     - `CertificateTypeField.java`: @TableName("UCMS_CERTIFICATE_TYPE_FIELD"), @TableId(type=IdType.AUTO), Lombok注解
     - `ValidationRuleConfig.java`: @TableName("UCMS_VALIDATION_RULE_CONFIG"), @TableId(type=IdType.AUTO), Lombok注解
     - `ValidationRuleItem.java`: @TableName("UCMS_VALIDATION_RULE_ITEM"), @TableId(type=IdType.AUTO), Lombok注解
     - `ScopeConfig.java`: @TableName("UCMS_SCOPE_CONFIG"), @TableId(type=IdType.AUTO), Lombok注解
   - 创建DTO类:
     - `CertificateTypeDTO.java`: 包含证件类型和字段配置的完整信息
     - `CertificateTypeFieldDTO.java`: 证件字段配置DTO
     - `PublicFieldDTO.java`: 公共字段DTO
     - `ValidationRuleConfigDTO.java`: 校验规则配置DTO（包含字段+逻辑+值）
     - `ValidationRuleItemDTO.java`: 校验规则明细DTO
     - `CertificateRequest.java`: 证件验证请求DTO（包含业务类型、证件类型、字段值等）
     - `CertificateValidationResponse.java`: 证件验证响应DTO
     - `ScopeConfigDTO.java`: 应用范围配置DTO
   - 创建枚举类:
     - `StatusEnum.java`: DISABLED（0）, ENABLED（1）
   - 添加MyBatis Plus注解: @TableName, @TableId, @TableField等

  **Must NOT do**:
  - 不要在实体类中添加业务逻辑
  - 不要使用DTO和实体类混用

  **Parallelizable**: NO (with 2, 4)

  **References** (CRITICAL - Be Exhaustive):

  **Pattern References** (existing code to follow):
  - 无（全新项目）

   **API/Type References** (contracts to implement against):
   - CHBN融合业务支撑项目_统一证件管理平台_产品需求说明书_0126.md: 证件类型配置、校验规则配置、应用范围配置

   **Test References** (testing patterns to follow):
   - 无（全新项目）

   **Documentation References** (specs and requirements):
   - AGENTS.md: 使用2空格缩进，变量名用驼峰命名，类名用帕斯卡命名

   **External References** (libraries and frameworks):
   - MyBatis Plus注解文档: https://baomidou.com/pages/581f0d/
   - Lombok文档: https://projectlombok.org/features/all

   **WHY Each Reference Matters** (explain the relevance):
   - 产需文档: 了解实体类的字段定义和类型
   - MyBatis Plus注解文档: 了解如何使用@TableName、@TableId、@TableField等注解进行ORM映射
   - Lombok文档: 了解如何使用Lombok减少样板代码

  **Acceptance Criteria**:

  **TDD**:
  - [ ] Test file created: `src/test/java/com/ucms/certificate/entity/CertificateTypeTest.java`
  - [ ] Test covers: 实体类可以正确序列化/反序列化为JSON
  - [ ] `mvn test` → PASS (2 tests, 0 failures)

  **Manual Execution Verification**:
  - [ ] Compile: `mvn compile`
  - [ ] Verify: 无编译错误
  - [ ] Verify: 所有实体类都有Lombok注解（@Data, @Entity等）

  **Evidence Required**:
  - [ ] 编译成功日志截图
  - [ ] 实体类代码片段截图

  **Commit**: YES
  - Message: `feat(entity): 创建实体类和DTO`
  - Files: src/main/java/com/ucms/certificate/entity/*.java, src/main/java/com/ucms/certificate/dto/*.java
  - Pre-commit: `mvn test`

 - [ ] 4. 配置MyBatis Plus和数据库连接

   **What to do**:
   - 配置application.yml:
     ```yaml
     spring:
       datasource:
         url: jdbc:h2:mem:testdb
         driver-class-name: org.h2.Driver
         username: sa
         password:
     mybatis-plus:
       configuration:
         log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
         map-underscore-to-camel-case: true
       global-config:
         db-config:
           table-prefix: UCMS_
           logic-delete-field: isDeleted
           logic-delete-value: 1
           logic-not-delete-value: 0
       mapper-locations: classpath:mapper/*.xml
       type-aliases-package: com.newland.ucms.certificate.entity
     ```
   - 配置OceanBase生产环境连接（在application-prod.yml中）:
     ```yaml
     spring:
       datasource:
         url: jdbc:oceanbase://<oceanbase-host>:<port>/<database>
         driver-class-name: com.alipay.oceanbase.jdbc.Driver
         username: <username>
         password: <password>
     mybatis-plus:
       configuration:
         log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
         map-underscore-to-camel-case: true
     ```

  **Must NOT do**:
  - 不要在代码中硬编码数据库密码（使用环境变量）
  - 不要在生产环境使用H2数据库

  **Parallelizable**: NO (with 2, 3)

  **References** (CRITICAL - Be Exhaustive):

  **Pattern References** (existing code to follow):
  - 无（全新项目）

  **API/Type References** (contracts to implement against):
  - 无（全新项目）

  **Test References** (testing patterns to follow):
  - 无（全新项目）

   **Documentation References** (specs and requirements):
   - AGENTS.md: OceanBase数据库（兼容MySQL协议）

   **External References** (libraries and frameworks):
   - Spring Boot配置文档: https://docs.spring.io/spring-boot/docs/3.3.2/reference/html/application-properties.html
   - MyBatis Plus配置文档: https://baomidou.com/pages/56bac0/
   - H2数据库文档: https://www.h2database.com/html/features.html

   **WHY Each Reference Matters** (explain the relevance):
   - Spring Boot配置文档: 了解如何配置数据源
   - MyBatis Plus配置文档: 了解如何配置MyBatis Plus（mapper扫描、类型别名、逻辑删除等）
   - H2数据库文档: 了解H2数据库的内存模式配置
   - OceanBase文档: 了解OceanBase的连接配置

  **Acceptance Criteria**:

  **TDD**:
  - [ ] Test file created: `src/test/java/com/ucms/certificate/config/DatabaseConnectionTest.java`
  - [ ] Test covers: 数据库连接成功，可以执行查询
  - [ ] `mvn test` → PASS (2 tests, 0 failures)

  **Manual Execution Verification**:
  - [ ] Run: `mvn spring-boot:run`
  - [ ] Verify: 启动成功，无数据库连接错误
  - [ ] Verify: H2控制台可访问（http://localhost:8080/h2-console）

  **Evidence Required**:
  - [ ] 启动日志截图（包含数据库连接成功的信息）
  - [ ] H2控制台截图（显示表结构）

  **Commit**: YES
  - Message: `feat(config): 配置MyBatis Plus和数据库连接`
  - Files: src/main/resources/application.yml, src/main/resources/application-prod.yml
  - Pre-commit: `mvn test`

### Phase 3: 后端核心功能开发

 - [ ] 5. 实现Mapper层

   **What to do**:
   - 创建Mapper接口（使用MyBatis Plus）:
     - `CertificateTypeMapper.java`: 继承BaseMapper<CertificateType>, 添加自定义查询方法: findByCertTypeCode, findAllByStatus
     - `PublicFieldLibraryMapper.java`: 继承BaseMapper<PublicFieldLibrary>, 添加查询方法: findAll
     - `CertificateTypeFieldMapper.java`: 继承BaseMapper<CertificateTypeField>, 添加查询方法: findByCertTypeCode
     - `ValidationRuleConfigMapper.java`: 继承BaseMapper<ValidationRuleConfig>, 添加查询方法: findByBusinessCode, findByChannelSubType
     - `ValidationRuleItemMapper.java`: 继承BaseMapper<ValidationRuleItem>, 添加查询方法: findByRuleId
     - `ScopeConfigMapper.java`: 继承BaseMapper<ScopeConfig>, 添加自定义查询方法: findByBusinessChannel, findMatchingScopeConfig（支持5层优先级）
   - 实现5层优先级查询逻辑（在ScopeConfigMapper.xml中）:
     ```xml
     <select id="findMatchingScopeConfig" resultType="com.newland.ucms.certificate.entity.ScopeConfig">
       SELECT * FROM UCMS_SCOPE_CONFIG
       WHERE is_deleted = 0
         AND status = 1
         AND (business_channel = #{businessChannel} OR business_channel IS NULL)
         AND (channel_sub_type = #{channelSubType} OR channel_sub_type IS NULL)
         AND (city = #{city} OR city IS NULL)
         AND (district = #{district} OR district IS NULL)
         AND (institution = #{institution} OR institution IS NULL)
       ORDER BY
         CASE WHEN institution IS NOT NULL THEN 0 ELSE 1 END,
         CASE WHEN district IS NOT NULL THEN 0 ELSE 1 END,
         CASE WHEN city IS NOT NULL THEN 0 ELSE 1 END,
         CASE WHEN channel_sub_type IS NOT NULL THEN 0 ELSE 1 END,
         CASE WHEN business_channel IS NOT NULL THEN 0 ELSE 1 END
       LIMIT 1
     </select>
     ```

   **Must NOT do**:
   - 不要在Mapper层添加业务逻辑
   - 5层优先级查询使用XML实现，不使用复杂条件构造器

   **Parallelizable**: NO (with 6, 7, depends on 2, 3, 4)

   **References** (CRITICAL - Be Exhaustive):

   **Pattern References** (existing code to follow):
   - 无（全新项目）

   **API/Type References** (contracts to implement against):
   - CertificateType.java, CertificateTypeField.java, ValidationRuleConfig.java, ScopeConfig.java

   **Test References** (testing patterns to follow):
   - 无（全新项目）

   **Documentation References** (specs and requirements):
   - AGENTS.md: 应用范围配置化（5层结构，最末级优先）

   **External References** (libraries and frameworks):
   - MyBatis Plus文档: https://baomidou.com/pages/49cc81/#mapper-crud-接口
   - MyBatis XML映射: https://mybatis.org/mybatis-3/zh/sqlmap-xml.html

   **WHY Each Reference Matters** (explain the relevance):
   - MyBatis Plus文档: 了解BaseMapper的CRUD接口和方法
   - MyBatis XML文档: 了解如何在XML中编写自定义SQL查询

  **Acceptance Criteria**:

  **TDD**:
  - [ ] Test file created: `src/test/java/com/ucms/certificate/repository/ScopeConfigMapperTest.java`
  - [ ] Test covers: 5层优先级查询逻辑正确（机构配置优先于区县，区县优先于地市，等等）
  - [ ] `mvn test` → PASS (5 tests, 0 failures)

  **Manual Execution Verification**:
  - [ ] Run: `mvn test`
  - [ ] Verify: 所有Repository测试通过
  - [ ] Verify: 5层优先级查询逻辑正确（通过测试用例验证）

  **Evidence Required**:
  - [ ] 测试通过日志截图
  - [ ] 测试用例代码片段（展示5层优先级测试）

  **Commit**: YES
  - Message: `feat(mapper): 实现Mapper层`
  - Files: src/main/java/com/ucms/certificate/mapper/*.java, resources/mapper/*.xml
  - Pre-commit: `mvn test`

- [ ] 6. 实现Service层

  **What to do**:
  - 创建Service接口和实现:
    - `CertificateTypeService.java` / `CertificateTypeServiceImpl.java`:
      - getAllCertificateTypes(): 获取所有证件类型
      - getCertificateTypeByCode(String certTypeCode): 根据代码获取证件类型
      - getCertificateTypeWithFields(String certTypeCode): 获取证件类型及字段配置
      - getValidationRules(String certTypeCode): 获取验证规则
    - `ScopeConfigService.java` / `ScopeConfigServiceImpl.java`:
      - createScopeConfig(ScopeConfigDTO dto): 创建应用范围配置
      - updateScopeConfig(Long id, ScopeConfigDTO dto): 更新应用范围配置
      - deleteScopeConfig(Long id): 删除应用范围配置
      - getScopeConfig(String businessChannel, String channelSubType, String city, String district, String institution): 根据5层参数获取应用范围配置
      - batchImportScopeConfig(List<ScopeConfigDTO> dtos): 批量导入应用范围配置
    - `ValidationService.java` / `ValidationServiceImpl.java`:
      - validateCertificate(CertificateRequest request): 统一证件验证入口
   - 实现业务逻辑:
     - CertificateTypeService: 调用Mapper获取数据，组装DTO
     - ScopeConfigService: 处理CRUD逻辑，调用Mapper查询匹配的配置
     - ValidationService: 调用ScopeConfigService获取允许的证件类型，调用ValidationRuleConfigMapper获取验证规则，执行验证逻辑

   **Must NOT do**:
   - 不要在Service层直接操作数据库（使用Mapper）
   - 不要在Service层返回实体类（使用DTO）

  **Parallelizable**: NO (with 5, 7, depends on 5)

  **References** (CRITICAL - Be Exhaustive):

  **Pattern References** (existing code to follow):
  - 无（全新项目）

  **API/Type References** (contracts to implement against):
  - CertificateTypeDTO.java, ScopeConfigDTO.java, CertificateRequest.java, CertificateValidationResponse.java

  **Test References** (testing patterns to follow):
  - 无（全新项目）

  **Documentation References** (specs and requirements):
  - 需求.md: 证件类型配置化、应用范围配置化、统一证件校验规则
  - 需求.md: 批量导入功能（Excel格式）

  **External References** (libraries and frameworks):
  - Spring Boot Service层最佳实践: https://spring.io/guides/gs/spring-boot/

  **WHY Each Reference Matters** (explain the relevance):
  - Spring Boot文档: 了解Service层的最佳实践

  **Acceptance Criteria**:

  **TDD**:
  - [ ] Test file created: `src/test/java/com/ucms/certificate/service/CertificateTypeServiceTest.java`
  - [ ] Test file created: `src/test/java/com/ucms/certificate/service/ScopeConfigServiceTest.java`
  - [ ] Test file created: `src/test/java/com/ucms/certificate/service/ValidationServiceTest.java`
  - [ ] Test covers: 所有Service方法逻辑正确，包括5层优先级查询、批量导入、证件验证
  - [ ] `mvn test` → PASS (10 tests, 0 failures)

  **Manual Execution Verification**:
  - [ ] Run: `mvn test`
  - [ ] Verify: 所有Service测试通过

  **Evidence Required**:
  - [ ] 测试通过日志截图
  - [ ] 测试用例代码片段（展示5层优先级和证件验证测试）

  **Commit**: YES
  - Message: `feat(service): 实现Service层`
  - Files: src/main/java/com/ucms/certificate/service/*.java
  - Pre-commit: `mvn test`

- [ ] 7. 实现Controller层（REST API）

  **What to do**:
  - 创建Controller类:
    - `CertificateTypeController.java`:
      - GET /api/certificate-types: 获取所有证件类型
      - GET /api/certificate-types/{certTypeCode}: 根据代码获取证件类型
      - GET /api/certificate-types/{certTypeCode}/fields: 获取字段配置
      - GET /api/certificate-types/{certTypeCode}/validation-rules: 获取验证规则
    - `ScopeConfigController.java`:
      - POST /api/scope-config: 创建应用范围配置
      - PUT /api/scope-config/{id}: 更新应用范围配置
      - DELETE /api/scope-config/{id}: 删除应用范围配置
      - GET /api/scope-config: 查询应用范围配置（支持过滤）
      - POST /api/scope-config/batch-import: 批量导入应用范围配置（Excel文件）
    - `ValidationController.java`:
      - POST /api/certificate/validate: 统一证件验证接口
  - 实现API Key认证:
    - 创建注解 @ApiRequired
    - 创建拦截器 ApiKeyInterceptor，检查请求头中的X-API-KEY
    - 配置拦截器到WebMvcConfigurer
  - 实现统一异常处理:
    - 创建GlobalExceptionHandler.java，处理BusinessException, ValidationException等
    - 返回统一的错误响应格式:
      ```json
      {
        "code": 400,
        "message": "错误信息",
        "timestamp": "2025-01-27T10:00:00"
      }
      ```

  **Must NOT do**:
  - 不要在Controller层添加业务逻辑
  - 不要返回实体类（使用DTO）
  - 不要使用默认的错误处理（使用GlobalExceptionHandler）

  **Parallelizable**: NO (with 5, 6, depends on 6)

  **References** (CRITICAL - Be Exhaustive):

  **Pattern References** (existing code to follow):
  - 无（全新项目）

  **API/Type References** (contracts to implement against):
  - CertificateTypeDTO.java, ScopeConfigDTO.java, CertificateRequest.java, CertificateValidationResponse.java

  **Test References** (testing patterns to follow):
  - 无（全新项目）

  **Documentation References** (specs and requirements):
  - 需求.md: REST API交互方式
  - 需求.md: API Key认证
  - AGENTS.md: 路径使用短横线分隔，HTTP方法严格遵循RESTful规范

  **External References** (libraries and frameworks):
  - Spring MVC文档: https://docs.spring.io/spring-framework/reference/web/webmvc.html
  - Spring Boot REST API最佳实践: https://spring.io/guides/gs/rest-service/

  **WHY Each Reference Matters** (explain the relevance):
  - Spring MVC文档: 了解Controller的定义和使用方法
  - Spring Boot REST API文档: 了解REST API的最佳实践

  **Acceptance Criteria**:

  **TDD**:
  - [ ] Test file created: `src/test/java/com/ucms/certificate/controller/CertificateTypeControllerTest.java`
  - [ ] Test file created: `src/test/java/com/ucms/certificate/controller/ScopeConfigControllerTest.java`
  - [ ] Test file created: `src/test/java/com/ucms/certificate/controller/ValidationControllerTest.java`
  - [ ] Test covers: 所有API接口正确响应，包括认证、异常处理、参数验证
  - [ ] `mvn test` → PASS (8 tests, 0 failures)

  **Manual Execution Verification**:
  - [ ] Run: `mvn spring-boot:run`
  - [ ] Verify: 使用curl测试API:
    - `curl -X GET http://localhost:8080/api/certificate-types -H "X-API-KEY: test-key"`
    - `curl -X POST http://localhost:8080/api/scope-config -H "Content-Type: application/json" -H "X-API-KEY: test-key" -d '{...}'`
    - `curl -X POST http://localhost:8080/api/certificate/validate -H "Content-Type: application/json" -H "X-API-KEY: test-key" -d '{...}'`
  - [ ] Verify: API响应正确，认证生效

  **Evidence Required**:
  - [ ] API测试命令和响应截图
  - [ ] 测试通过日志截图

  **Commit**: YES
  - Message: `feat(controller): 实现Controller层（REST API）`
  - Files: src/main/java/com/ucms/certificate/controller/*.java, src/main/java/com/ucms/certificate/config/*.java
  - Pre-commit: `mvn test`

- [ ] 8. 实现统一验证服务

  **What to do**:
  - 实现证件验证核心逻辑:
    - 根据请求中的办理通路、渠道小类、地市、区县、机构信息，调用ScopeConfigService获取允许的证件类型
    - 根据请求中的证件类型，调用CertificateTypeService获取字段配置和验证规则
    - 验证请求中的证件字段是否符合配置（必填、格式等）
    - 返回验证结果（成功/失败，错误信息）
  - 实现验证规则引擎:
    - 使用Spring Validator框架，根据动态配置执行验证
    - 支持自定义验证器（如身份证格式验证）
    - 缓存验证规则（使用Spring Cache）
  - 实现验证错误处理:
    - 收集所有验证错误，返回详细的错误信息
    - 支持字段级别的错误提示

  **Must NOT do**:
  - 不要硬编码验证规则（必须从数据库动态加载）
  - 不要在前端验证规则（验证逻辑必须在后端）

  **Parallelizable**: NO (depends on 6)

  **References** (CRITICAL - Be Exhaustive):

  **Pattern References** (existing code to follow):
  - 无（全新项目）

   **API/Type References** (contracts to implement against):
   - CertificateRequest.java, CertificateValidationResponse.java
   - CertificateTypeField.java（字段配置）
   - ValidationRuleConfig.java, ValidationRuleItem.java（验证规则）

  **Test References** (testing patterns to follow):
  - 无（全新项目）

  **Documentation References** (specs and requirements):
  - 需求.md: 统一证件校验规则
  - 需求.md: 15种证件类型的验证规则表

  **External References** (libraries and frameworks):
  - Spring Validator文档: https://docs.spring.io/spring-framework/reference/core/validation/validator.html
  - Spring Cache文档: https://docs.spring.io/spring-framework/reference/integration/cache.html

  **WHY Each Reference Matters** (explain the relevance):
  - Spring Validator文档: 了解如何使用Spring Validator框架实现动态验证
  - Spring Cache文档: 了解如何使用Spring Cache缓存验证规则

  **Acceptance Criteria**:

  **TDD**:
  - [ ] Test file created: `src/test/java/com/ucms/certificate/service/ValidationServiceTest.java`
  - [ ] Test covers: 15种证件类型的验证逻辑正确，包括字段验证、格式验证、5层优先级查询
  - [ ] Test covers: 验证规则缓存生效
  - [ ] `mvn test` → PASS (15 tests, 0 failures)

  **Manual Execution Verification**:
  - [ ] Run: `mvn test`
  - [ ] Verify: 所有验证服务测试通过
  - [ ] Run: `curl -X POST http://localhost:8080/api/certificate/validate -H "Content-Type: application/json" -H "X-API-KEY: test-key" -d '{...}'`
  - [ ] Verify: 验证接口正确返回验证结果

  **Evidence Required**:
  - [ ] 测试通过日志截图
  - [ ] API测试命令和响应截图
  - [ ] 验证规则缓存效果截图（通过日志验证缓存命中）

  **Commit**: YES
  - Message: `feat(validation): 实现统一验证服务`
  - Files: src/main/java/com/ucms/certificate/service/ValidationServiceImpl.java, src/main/java/com/ucms/certificate/validator/*.java
  - Pre-commit: `mvn test`

- [ ] 9. 实现外部数据集成

  **What to do**:
  - 创建外部API客户端:
    - `BusinessChannelClient.java`: 调用外部API获取办理通路信息
    - `ChannelSubTypeClient.java`: 调用外部API获取渠道小类信息
    - `CityClient.java`: 调用外部API获取地市信息
    - `DistrictClient.java`: 调用外部API获取区县信息
    - `InstitutionClient.java`: 调用外部API获取机构信息
  - 实现外部数据缓存:
    - 使用Spring Cache缓存外部数据
    - 配置缓存过期时间（TTL: 30分钟）
  - 实现外部数据服务:
    - `ExternalDataService.java`: 封装所有外部数据调用，提供统一的接口

  **Must NOT do**:
  - 不要同步外部数据到本地数据库（使用实时拉取模式）
  - 不要在验证服务中直接调用外部API（使用ExternalDataService封装）

  **Parallelizable**: NO (depends on 8)

  **References** (CRITICAL - Be Exhaustive):

  **Pattern References** (existing code to follow):
  - 无（全新项目）

  **API/Type References** (contracts to implement against):
  - 无（外部API由用户配置）

  **Test References** (testing patterns to follow):
  - 无（全新项目）

  **Documentation References** (specs and requirements):
  - 需求.md: 外部数据集成（实时拉取模式）
  - 需求.md: 外部API地址配置

  **External References** (libraries and frameworks):
  - Spring RestTemplate文档: https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-resttemplate
  - Spring WebClient文档: https://docs.spring.io/spring-framework/reference/web/webflux.html#webflux-client

  **WHY Each Reference Matters** (explain the relevance):
  - Spring RestTemplate文档: 了解如何使用RestTemplate调用外部API
  - Spring WebClient文档: 了解如何使用WebClient进行异步HTTP调用

  **Acceptance Criteria**:

  **TDD**:
  - [ ] Test file created: `src/test/java/com/ucms/certificate/client/ExternalDataClientTest.java`
  - [ ] Test covers: 外部API调用成功，缓存生效
  - [ ] `mvn test` → PASS (5 tests, 0 failures)

  **Manual Execution Verification**:
  - [ ] Run: `mvn test`
  - [ ] Verify: 所有外部数据客户端测试通过
  - [ ] Verify: 缓存生效（通过日志验证缓存命中）

  **Evidence Required**:
  - [ ] 测试通过日志截图
  - [ ] 外部API调用日志截图
  - [ ] 缓存效果截图

  **Commit**: YES
  - Message: `feat(external): 实现外部数据集成`
  - Files: src/main/java/com/ucms/certificate/client/*.java, src/main/java/com/ucms/certificate/service/ExternalDataService.java
  - Pre-commit: `mvn test`

- [ ] 10. 后端单元测试覆盖率验证

  **What to do**:
  - 运行所有单元测试: `mvn test`
  - 生成测试覆盖率报告: `mvn jacoco:report`
  - 验证覆盖率是否达到80%以上
  - 如果覆盖率不足，补充测试用例

  **Must NOT do**:
  - 不要伪造测试用例
  - 不要忽略失败的测试

  **Parallelizable**: NO (depends on 7, 8, 9)

  **References** (CRITICAL - Be Exhaustive):

  **Pattern References** (existing code to follow):
  - 无（全新项目）

  **API/Type References** (contracts to implement against):
  - 无（覆盖率验证）

  **Test References** (testing patterns to follow):
  - 无（覆盖率验证）

  **Documentation References** (specs and requirements):
  - AGENTS.md: 单元测试覆盖率80%以上

  **External References** (libraries and frameworks):
  - JaCoCo文档: https://www.jacoco.org/jacoco/trunk/doc/

  **WHY Each Reference Matters** (explain the relevance):
  - JaCoCo文档: 了解如何生成测试覆盖率报告

  **Acceptance Criteria**:

  **TDD**:
  - [ ] Run: `mvn test`
  - [ ] Run: `mvn jacoco:report`
  - [ ] Verify: 测试覆盖率 >= 80%

  **Manual Execution Verification**:
  - [ ] Run: `mvn clean test jacoco:report`
  - [ ] Verify: 打开 target/site/jacoco/index.html，查看覆盖率报告
  - [ ] Verify: 整体覆盖率 >= 80%

  **Evidence Required**:
  - [ ] 测试通过日志截图
  - [ ] 覆盖率报告截图（显示80%以上）

  **Commit**: NO (coverage verification only)

### Phase 4: 前端项目初始化

- [ ] 11. 初始化Vue 3前端项目

  **What to do**:
  - 使用Vite创建Vue 3项目: `npm create vite@latest frontend -- --template vue-ts`
  - 安装依赖:
    - Element Plus: `npm install element-plus`
    - Axios: `npm install axios`
    - Vue Router: `npm install vue-router`
    - Pinia: `npm install pinia`
  - 配置项目:
    - 配置Element Plus（按需引入或全局引入）
    - 配置Axios（ baseURL, timeout, 拦截器）
    - 配置Vue Router（路由表）
    - 配置Pinia（状态管理）
  - 创建基础目录结构: src/{{components, views, api, router, store, utils, types}}

  **Must NOT do**:
  - 不要安装不必要的依赖
  - 不要使用Vue 2语法

  **Parallelizable**: NO (must start after backend)

  **References** (CRITICAL - Be Exhaustive):

  **Pattern References** (existing code to follow):
  - 无（全新项目）

  **API/Type References** (contracts to implement against):
  - 无（全新项目）

  **Test References** (testing patterns to follow):
  - 无（全新项目）

  **Documentation References** (specs and requirements):
  - AGENTS.md: Vue 3.x, Element Plus, REST API
  - AGENTS.md: 2空格缩进，驼峰命名，帕斯卡命名

  **External References** (libraries and frameworks):
  - Vue 3文档: https://cn.vuejs.org/
  - Vite文档: https://cn.vitejs.dev/
  - Element Plus文档: https://element-plus.org/zh-CN/
  - Axios文档: https://axios-http.com/

  **WHY Each Reference Matters** (explain the relevance):
  - Vue 3文档: 了解Vue 3的 Composition API 和响应式系统
  - Element Plus文档: 了解如何使用Element Plus组件
  - Axios文档: 了解如何配置和使用Axios

  **Acceptance Criteria**:

  **TDD**:
  - [ ] Run: `npm run dev`
  - [ ] Verify: 项目启动成功，浏览器访问 http://localhost:5173 显示默认页面
  - [ ] Run: `npm run test` (after setting up test infra)

  **Manual Execution Verification**:
  - [ ] Run: `npm install`
  - [ ] Run: `npm run dev`
  - [ ] Verify: 控制台无错误信息
  - [ ] Verify: 浏览器显示Vue默认页面

  **Evidence Required**:
  - [ ] 项目启动截图
  - [ ] 浏览器访问截图

  **Commit**: YES
  - Message: `feat(frontend): 初始化Vue 3项目`
  - Files: package.json, vite.config.ts, src/main.ts, src/App.vue
  - Pre-commit: `npm run lint`

### Phase 5: 前端核心功能开发

- [ ] 12. 创建证件类型配置界面

  **What to do**:
  - 创建页面组件 `src/views/CertificateTypeConfig.vue`:
    - 表格展示所有证件类型（Element Plus Table）
    - 编辑对话框（Element Plus Dialog + Form）
    - 新增/编辑/删除功能
  - 创建字段配置组件 `src/components/CertificateFieldConfig.vue`:
    - 表格展示字段配置
    - 编辑对话框（必填、类型、正则表达式等）
  - 创建API封装 `src/api/certificateType.ts`:
    - getCertificateTypes(): GET /api/certificate-types
    - getCertificateType(certTypeCode): GET /api/certificate-types/{certTypeCode}
    - updateCertificateType(certTypeCode, data): PUT /api/certificate-types/{certTypeCode}

  **Must NOT do**:
  - 不要硬编码验证规则
  - 不要在前端验证（仅用于UI提示，真实验证在后端）

  **Parallelizable**: NO (with 13, depends on 11)

  **References** (CRITICAL - Be Exhaustive):

  **Pattern References** (existing code to follow):
  - 无（全新项目）

  **API/Type References** (contracts to implement against):
  - 后端API: GET /api/certificate-types, GET /api/certificate-types/{certTypeCode}, PUT /api/certificate-types/{certTypeCode}
  - CertificateTypeDTO.java

  **Test References** (testing patterns to follow):
  - 无（全新项目）

  **Documentation References** (specs and requirements):
  - 需求.md: 证件类型配置界面

  **External References** (libraries and frameworks):
  - Element Plus Table组件: https://element-plus.org/zh-CN/component/table.html
  - Element Plus Form组件: https://element-plus.org/zh-CN/component/form.html
  - Element Plus Dialog组件: https://element-plus.org/zh-CN/component/dialog.html

  **WHY Each Reference Matters** (explain the relevance):
  - Element Plus文档: 了解如何使用Table、Form、Dialog组件

  **Acceptance Criteria**:

  **TDD**:
  - [ ] Test file created: `src/__tests__/CertificateTypeConfig.spec.ts`
  - [ ] Test covers: 组件渲染正确，新增/编辑/删除功能正常
  - [ ] `npm run test` → PASS (3 tests, 0 failures)

  **Manual Execution Verification**:
  - [ ] Run: `npm run dev`
  - [ ] Navigate to: http://localhost:5173/certificate-types
  - [ ] Verify: 页面显示证件类型列表
  - [ ] Verify: 点击"新增"按钮，弹出对话框
  - [ ] Verify: 点击"编辑"按钮，弹出对话框并填充数据
  - [ ] Verify: 点击"删除"按钮，确认后删除成功

  **Evidence Required**:
  - [ ] 页面截图（证件类型列表）
  - [ ] 操作截图（新增、编辑、删除）

  **Commit**: YES
  - Message: `feat(frontend): 创建证件类型配置界面`
  - Files: src/views/CertificateTypeConfig.vue, src/components/CertificateFieldConfig.vue, src/api/certificateType.ts
  - Pre-commit: `npm run test && npm run lint`

- [ ] 13. 创建应用范围配置界面

  **What to do**:
  - 创建页面组件 `src/views/ScopeConfig.vue`:
    - 表格展示应用范围配置（支持过滤）
    - 编辑对话框（5层配置：办理通路、渠道小类、地市、区县、机构）
    - 批量导入功能（Excel文件上传）
    - 新增/编辑/删除功能
  - 创建批量导入组件 `src/components/ScopeConfigImport.vue`:
    - Excel文件上传（Element Plus Upload）
    - 预览导入数据
    - 确认导入
  - 创建API封装 `src/api/scopeConfig.ts`:
    - getScopeConfigs(): GET /api/scope-config
    - createScopeConfig(data): POST /api/scope-config
    - updateScopeConfig(id, data): PUT /api/scope-config/{id}
    - deleteScopeConfig(id): DELETE /api/scope-config/{id}
    - batchImport(file): POST /api/scope-config/batch-import

  **Must NOT do**:
  - 不要在前端实现5层优先级逻辑（由后端处理）

  **Parallelizable**: NO (with 12, depends on 11)

  **References** (CRITICAL - Be Exhaustive):

  **Pattern References** (existing code to follow):
  - 无（全新项目）

  **API/Type References** (contracts to implement against):
  - 后端API: GET /api/scope-config, POST /api/scope-config, PUT /api/scope-config/{id}, DELETE /api/scope-config/{id}, POST /api/scope-config/batch-import
  - ScopeConfigDTO.java

  **Test References** (testing patterns to follow):
  - 无（全新项目）

  **Documentation References** (specs and requirements):
  - 需求.md: 证件应用范围配置化
  - 需求.md: 批量导入功能（Excel格式）

  **External References** (libraries and frameworks):
  - Element Plus Upload组件: https://element-plus.org/zh-CN/component/upload.html
  - Element Plus Table组件: https://element-plus.org/zh-CN/component/table.html
  - xlsx库: https://github.com/SheetJS/sheetjs (用于解析Excel)

  **WHY Each Reference Matters** (explain the relevance):
  - Element Plus文档: 了解如何使用Upload和Table组件
  - xlsx库: 了解如何解析Excel文件

  **Acceptance Criteria**:

  **TDD**:
  - [ ] Test file created: `src/__tests__/ScopeConfig.spec.ts`
  - [ ] Test covers: 组件渲染正确，新增/编辑/删除/批量导入功能正常
  - [ ] `npm run test` → PASS (4 tests, 0 failures)

  **Manual Execution Verification**:
  - [ ] Run: `npm run dev`
  - [ ] Navigate to: http://localhost:5173/scope-configs
  - [ ] Verify: 页面显示应用范围配置列表
  - [ ] Verify: 点击"新增"按钮，弹出对话框
  - [ ] Verify: 上传Excel文件，预览导入数据
  - [ ] Verify: 点击"导入"按钮，导入成功

  **Evidence Required**:
  - [ ] 页面截图（应用范围配置列表）
  - [ ] 操作截图（新增、批量导入）

  **Commit**: YES
  - Message: `feat(frontend): 创建应用范围配置界面`
  - Files: src/views/ScopeConfig.vue, src/components/ScopeConfigImport.vue, src/api/scopeConfig.ts
  - Pre-commit: `npm run test && npm run lint`

- [ ] 14. 实现动态表单渲染

  **What to do**:
  - 创建动态表单组件 `src/components/DynamicForm.vue`:
    - 根据后端返回的字段配置动态生成表单
    - 支持不同字段类型（text, number, date等）
    - 集成Element Plus表单验证规则
  - 创建统一验证页面 `src/views/ValidationTest.vue`:
    - 选择证件类型
    - 动态显示表单字段
    - 填写证件信息
    - 调用统一验证接口
    - 显示验证结果
  - 创建API封装 `src/api/validation.ts`:
    - validateCertificate(data): POST /api/certificate/validate

  **Must NOT do**:
  - 不要在前端实现验证规则（仅用于UI提示，真实验证在后端）

  **Parallelizable**: NO (depends on 12, 13)

  **References** (CRITICAL - Be Exhaustive):

  **Pattern References** (existing code to follow):
  - 无（全新项目）

  **API/Type References** (contracts to implement against):
  - 后端API: GET /api/certificate-types/{certTypeCode}/fields, POST /api/certificate/validate
  - CertificateTypeField.java

  **Test References** (testing patterns to follow):
  - 无（全新项目）

  **Documentation References** (specs and requirements):
  - 需求.md: 统一证件校验规则

  **External References** (libraries and frameworks):
  - Element Plus Form组件: https://element-plus.org/zh-CN/component/form.html
  - 动态表单最佳实践: https://cn.vuejs.org/guide/essentials/reactivity-fundamentals.html

  **WHY Each Reference Matters** (explain the relevance):
  - Element Plus文档: 了解如何使用Form组件和验证规则
  - Vue 3文档: 了解如何实现动态表单

  **Acceptance Criteria**:

  **TDD**:
  - [ ] Test file created: `src/__tests__/DynamicForm.spec.ts`
  - [ ] Test covers: 动态表单正确渲染，验证规则正确应用
  - [ ] `npm run test` → PASS (2 tests, 0 failures)

  **Manual Execution Verification**:
  - [ ] Run: `npm run dev`
  - [ ] Navigate to: http://localhost:5173/validation-test
  - [ ] Verify: 选择证件类型后，动态显示对应的表单字段
  - [ ] Verify: 填写表单后，调用验证接口，显示验证结果

  **Evidence Required**:
  - [ ] 页面截图（动态表单渲染）
  - [ ] 操作截图（填写表单、验证）

  **Commit**: YES
  - Message: `feat(frontend): 实现动态表单渲染`
  - Files: src/components/DynamicForm.vue, src/views/ValidationTest.vue, src/api/validation.ts
  - Pre-commit: `npm run test && npm run lint`

- [ ] 15. 前端测试覆盖率验证

  **What to do**:
  - 运行所有前端测试: `npm run test`
  - 生成测试覆盖率报告: `npm run test:coverage`
  - 验证覆盖率是否达到80%以上
  - 如果覆盖率不足，补充测试用例

  **Must NOT do**:
  - 不要伪造测试用例
  - 不要忽略失败的测试

  **Parallelizable**: NO (depends on 12, 13, 14)

  **References** (CRITICAL - Be Exhaustive):

  **Pattern References** (existing code to follow):
  - 无（全新项目）

  **API/Type References** (contracts to implement against):
  - 无（覆盖率验证）

  **Test References** (testing patterns to follow):
  - 无（覆盖率验证）

  **Documentation References** (specs and requirements):
  - AGENTS.md: 单元测试覆盖率80%以上

  **External References** (libraries and frameworks):
  - Vitest文档: https://vitest.dev/guide/

  **WHY Each Reference Matters** (explain the relevance):
  - Vitest文档: 了解如何生成测试覆盖率报告

  **Acceptance Criteria**:

  **TDD**:
  - [ ] Run: `npm run test`
  - [ ] Run: `npm run test:coverage`
  - [ ] Verify: 测试覆盖率 >= 80%

  **Manual Execution Verification**:
  - [ ] Run: `npm run test:coverage`
  - [ ] Verify: 打开 coverage/index.html，查看覆盖率报告
  - [ ] Verify: 整体覆盖率 >= 80%

  **Evidence Required**:
  - [ ] 测试通过日志截图
  - [ ] 覆盖率报告截图（显示80%以上）

  **Commit**: NO (coverage verification only)

### Phase 6: 端到端测试

- [ ] 16. 端到端测试/

  **What to do**:
  - 创建E2E测试场景:
    - 场景1: 配置证件类型 → 配置应用范围 → 调用验证接口
    - 场景2: 批量导入应用范围配置 → 验证导入结果
    - 场景3: 5层优先级验证（机构配置优先于区县，区县优先于地市，等等）
  - 使用Postman或curl进行API测试
  - 使用Playwright或手动测试前端界面

  **Must NOT do**:
  - 不要跳过任何测试场景

  **Parallelizable**: NO (depends on 10, 15)

  **References** (CRITICAL - Be Exhaustive):

  **Pattern References** (existing code to follow):
  - 无（全新项目）

  **API/Type References** (contracts to implement against):
  - 所有后端API接口

  **Test References** (testing patterns to follow):
  - 无（全新项目）

  **Documentation References** (specs and requirements):
  - 需求.md: 所有核心需求

  **External References** (libraries and frameworks):
  - Playwright文档: https://playwright.dev/

  **WHY Each Reference Matters** (explain the relevance):
  - Playwright文档: 了解如何进行端到端测试

  **Acceptance Criteria**:

  **TDD**:
  - [ ] Test file created: `tests/e2e/certificate-validation.spec.ts`
  - [ ] Test covers: 所有E2E测试场景通过
  - [ ] `npm run test:e2e` → PASS (3 scenarios, 0 failures)

  **Manual Execution Verification**:
  - [ ] Run: `mvn spring-boot:run` (后端)
  - [ ] Run: `npm run dev` (前端)
  - [ ] Run: `npm run test:e2e`
  - [ ] Verify: 所有E2E测试通过
  - [ ] Manual test: 在浏览器中手动测试所有核心功能

  **Evidence Required**:
  - [ ] E2E测试通过日志截图
  - [ ] 手动测试截图（所有核心功能）

  **Commit**: NO (e2e test only)

### Phase 7: 文档和交付

- [ ] 17. 编写文档

  **What to do**:
  - 编写API文档（使用Swagger或OpenAPI）:
    - 所有API接口的请求/响应示例
    - API Key使用说明
  - 编写部署文档:
    - 后端部署步骤（Spring Boot打包、启动命令）
    - 前端部署步骤（构建、静态文件部署）
    - 数据库初始化步骤
    - 配置文件说明
  - 编写用户手册:
    - 证件类型配置使用说明
    - 应用范围配置使用说明
    - 批量导入使用说明

  **Must NOT do**:
  - 不要编写过时的文档

  **Parallelizable**: NO (depends on 16)

  **References** (CRITICAL - Be Exhaustive):

  **Pattern References** (existing code to follow):
  - 无（全新项目）

  **API/Type References** (contracts to implement against):
  - 所有后端API接口
  - 所有前端页面组件

  **Test References** (testing patterns to follow):
  - 无（文档编写）

  **Documentation References** (specs and requirements):
  - AGENTS.md: 代码注释使用中文

  **External References** (libraries and frameworks):
  - Swagger文档: https://swagger.io/
  - OpenAPI规范: https://spec.openapis.org/oas/latest.html

  **WHY Each Reference Matters** (explain the relevance):
  - Swagger文档: 了解如何使用Swagger生成API文档
  - OpenAPI规范: 了解OpenAPI规范格式

  **Acceptance Criteria**:

  **Manual Execution Verification**:
  - [ ] Verify: API文档包含所有接口，示例可执行
  - [ ] Verify: 部署文档步骤清晰，可按步骤部署
  - [ ] Verify: 用户手册图文并茂，易于理解

  **Evidence Required**:
  - [ ] API文档截图
  - [ ] 部署文档截图
  - [ ] 用户手册截图

  **Commit**: YES
  - Message: `docs: 编写API文档、部署文档和用户手册`
  - Files: docs/api.md, docs/deployment.md, docs/user-manual.md
  - Pre-commit: none

---

## Commit Strategy

| After Task | Message | Files | Verification |
|------------|---------|-------|--------------|
| 1 | feat(backend): 初始化Spring Boot项目 | pom.xml, application.yml, CertificateApplication.java | mvn test |
| 2 | feat(db): 设计并创建数据库表结构 | schema.sql, data.sql | mvn test |
| 3 | feat(entity): 创建实体类和DTO | entity/*.java, dto/*.java | mvn test |
| 4 | feat(config): 配置MyBatis Plus和数据库连接 | application.yml, | mvn test |
| 5 | feat(mapper): 实现Mapper层 | mapper/*.java, resources/mapper/*.xml | mvn test |
| 6 | feat(service): 实现Service层 | service/*.java | mvn test |
| 7 | feat(controller): 实现Controller层（REST API） | controller/*.java, config/*.java | mvn test |
| 8 | feat(validation): 实现统一验证服务 | ValidationServiceImpl.java, validator/*.java | mvn test |
| 9 | feat(external): 实现外部数据集成 | client/*.java, ExternalDataService.java | mvn test |
| 11 | feat(frontend): 初始化Vue 3项目 | package.json, vite.config.ts, main.ts, App.vue | npm run lint |
| 12 | feat(frontend): 创建证件类型配置界面 | CertificateTypeConfig.vue, CertificateFieldConfig.vue, certificateType.ts | npm run test && npm run lint |
| 13 | feat(frontend): 创建应用范围配置界面 | ScopeConfig.vue, ScopeConfigImport.vue, scopeConfig.ts | npm run test && npm run lint |
| 14 | feat(frontend): 实现动态表单渲染 | DynamicForm.vue, ValidationTest.vue, validation.ts | npm run test && npm run lint |
| 17 | docs: 编写API文档、部署文档和用户手册 | docs/api.md, docs/deployment.md, docs/user-manual.md | none |

---

## Success Criteria

### Verification Commands

**后端**:
```bash
# 编译项目
mvn clean compile

# 运行测试
mvn test

# 生成测试覆盖率报告
mvn jacoco:report

# 打包项目
mvn clean package

# 运行应用
mvn spring-boot:run

# API测试
curl -X GET http://localhost:8080/api/certificate-types -H "X-API-KEY: test-key"
curl -X POST http://localhost:8080/api/certificate/validate -H "Content-Type: application/json" -H "X-API-KEY: test-key" -d '{...}'
```

**前端**:
```bash
# 安装依赖
npm install

# 开发模式运行
npm run dev

# 生产构建
npm run build

# 运行测试
npm run test

# 生成测试覆盖率报告
npm run test:coverage

# 代码检查
npm run lint
```

### Final Checklist

- [ ] 所有15种证件类型的配置数据已初始化
- [ ] 后端所有API接口已实现并通过单元测试
- [ ] 前端所有管理界面已实现并通过集成测试
- [ ] 统一验证接口已实现并通过端到端测试
- [ ] 单元测试覆盖率达到80%以上
- [ ] 所有接口文档已编写完成
- [ ] 系统可通过API Key正常调用验证接口
- [ ] 批量导入功能正常工作
- [ ] 5层优先级查询逻辑正确
- [ ] 外部数据实时拉取功能正常工作
- [ ] 系统可以在OceanBase数据库上正常运行
- [ ] 前端可以在生产环境中正常访问后端API
