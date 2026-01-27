# AGENTS.md - 统一人证核验系统开发指南

## 项目概述
这是一个统一证件验证中台系统,用于电信运营商个人客户证件核验,支持多种证件类型的灵活配置和验证。
- 维护证件类型，以及每个证件类型又哪些字段。
- 配置证件应用范围配置，支持按办理通路（如云厅、网格通、便利店等，不含全网渠道，如京东OAO）、渠道小类、渠道小类、地市、区县、机构配置可使用的证件类型。
  外部数据集成：
 springboot配置外部API地址，获取办理通路信息
 springboot配置外部API地址，获取渠道小类信息
 springboot配置外部API地址，获取地市、区县信息
 springboot配置外部API地址，获取机构信息
- 本系统只做配置管理，（证件类型配置、应用范围配置、验证规则配置、统一验证接口），证件信息读取由业务受理平台读取，然后调用本系统 统一验证接口，本系统通过通过请求报文中的通路信息，或者渠道小类、渠道小类、地市、区县、机构信息（非必传）匹配出证件，然后根据证件的字段和报文中的事实数据进行校验。


## 技术栈

### 后端
- **框架**: Spring Boot 3.3.2
- **JDK版本**: 17
- **交互方式**: REST API

### 前端
- **框架**: Vue 3.x
- **UI组件库**: Element Plus
- **交互方式**: REST API

## 构建和测试命令

### 后端
```bash
# 编译项目
mvn clean compile

# 运行测试
mvn test

# 运行单个测试
mvn test -Dtest=ClassName#methodName

# 打包项目
mvn clean package

# 运行应用
mvn spring-boot:run
```

### 前端
```bash
# 安装依赖
npm install

# 开发模式运行
npm run dev

# 生产构建
npm run build

# 运行测试
npm run test

# 代码检查
npm run lint
```

## 代码风格指南

### 通用规范
- 使用 **2空格** 缩进
- 变量名使用 **驼峰命名** (camelCase)
- 函数名使用 **动词开头** (如 validateIdCard, getCertTypeConfig)
- 所有代码注释使用 **中文**
- 文件名使用 **帕斯卡命名** (PascalCase) 用于类/组件, **短横线命名** (kebab-case) 用于普通文件

### 后端 (Java/Spring Boot)
```java
// 类名: PascalCase
public class CertificateValidator { }

// 方法名: camelCase, 动词开头
public ValidationResult validateCertificate(CertificateRequest request) { }

// 常量: 全大写,下划线分隔
private static final int MAX_CERT_LENGTH = 18;

// 变量: camelCase
private String certNumber;

// 异常处理: 不使用空catch块
try {
    // ...
} catch (ValidationException e) {
    log.error("证件验证失败: {}", e.getMessage());
    throw new BusinessException("证件验证失败", e);
}
```

### 前端 (Vue 3)
```javascript
// 组件名: PascalCase
export default defineComponent({
  name: 'CertificateForm'
})

// 方法名: camelCase, 动词开头
const validateForm = () => { }

// 变量: camelCase
const certType = ref('')

// 常量: 全大写,下划线分隔
const MAX_RETRY_COUNT = 3
```

## 命名约定

### 后端接口
- 路径: 使用短横线分隔 `/certificate/validate`
- HTTP方法: 严格遵循RESTful规范
  - GET: 查询
  - POST: 创建
  - PUT: 更新
  - DELETE: 删除

### 数据库
- 表名: 下划线分隔,小写 `certificate_config`
- 字段名: 下划线分隔,小写 `cert_type`, `is_required`

### 前端组件
- 组件文件: PascalCase `CertificateForm.vue`
- 工具文件: 短横线分隔 `certificate-validator.js`
- API文件: 短横线分隔 `certificate-api.js`

## 错误处理

### 后端
- 不使用空catch块
- 使用统一的异常处理机制
- 自定义业务异常: `BusinessException`
- 验证异常: `ValidationException`

### 前端
- 使用Element Plus的消息提示: `ElMessage.error('错误信息')`
- 使用try-catch处理异步操作
- 提供用户友好的错误提示

## 类型安全

### 后端
- 使用Java类型系统,不使用 `@SuppressWarnings("unchecked")`
- 优先使用泛型
- 使用 `Optional` 处理可能为空的值

### 前端
- 使用TypeScript (推荐)
- 使用Prop类型定义
- 避免使用 `any` 类型

## 证件验证规则

- 统一证件类型配置管理
- 按证件类型动态校验规则
- 支持机读和自定义采集方式
- 人证比对功能支持

## 安全要求

- 敏感信息(证件号)脱敏处理
- 前端加密传输
- 后端数据加密存储
- 严格的权限控制

## 测试要求

- 单元测试覆盖率: 80%以上
- 每个证件验证规则必须有测试用例
- API接口需要集成测试
- 关键业务流程需要端到端测试

## 代码审查要点

1. **命名规范性**: 类、方法、变量命名是否清晰
2. **类型安全**: 是否使用了不安全的类型转换
3. **异常处理**: 是否有空的catch块
4. **安全性**: 敏感信息是否正确处理
5. **性能**: 是否有不必要的重复计算或查询
6. **可维护性**: 代码是否易于理解和修改
