# 基于AOP的DTO字典翻译demo

## 项目介绍
本方案通过 **Spring AOP** 和 **注解驱动设计** 实现字典值的自动翻译与透明注入，核心目标是在不修改原始业务对象（POJO）的前提下，将存储的字典编码（如 `departmentId`）动态转换为可读的字典文本（如 `departmentId_text`），并通过统一的响应包装类 `Result` 返回。所有翻译逻辑由切面层统一处理，业务代码完全无侵入。

## 核心步骤

### 1. 注解标记字典字段
- **组件**: `@Dict` 注解
- **功能**: 在POJO字段上声明需翻译的字典名称。
- **示例**:
  ```java
  public class User {  
      @Dict(dictName = "department")  
      private int departmentId;  
  }
  ```
### 2. 统一响应包装类
- **组件**: Result<T>
- **功能**: 封装接口返回数据，提供标准化结构（状态码、消息、业务数据）。
- **示例**:
  ```java
  public class Result<T> {  
    public Result(int code, String msg, T data);  
    public T getData();  
    public void setData(T data);  
  }
  ```
### 3. AOP切面拦截与注入
- **组件**: DictAspect
- **流程**:
  - 拦截Controller返回值:
  通过切点表达式定位所有返回 Result 对象的Controller方法。
  - 解析数据层:
  提取 Result.data 中的POJO对象（如 User），反射扫描 @Dict 注解字段。
  - 字典翻译:
  调用 DictManager 查询字典文本，生成 原字段名 + "_text" 的键值对。
  - 数据替换:
  将翻译结果注入到 Result.data 的JSON副本中，原始POJO对象保持未修改。

```java
public class DictAspect {
    @Pointcut(value = "execution(* org.example.UserController.*(..))")
    public void translateDict() {}
    
    @Around(value = "translateDict()")
    public Object test(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("执行切面方法");
        Object object = joinPoint.proceed();
        if (object instanceof Result<?>) {
            Object data = ((Result<?>) object).getData();
            Class<?> clazz = data.getClass();
            JSONObject jo = (JSONObject) JSON.toJSON(data);
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Dict.class)) {
                    String dictFieldName = field.getName() + "_text";
                    String dictText = DictManager.getDictItem(field.getDeclaredAnnotation(Dict.class).dictName(), field.getInt(data));
                    jo.put(dictFieldName, dictText);
                }
            }
            ((Result) object).setData(jo);
        }
        return object;
    }
}
```

### 3. 字典管理
   组件: DictManager (模拟数据库场景)

## 运行结果

```java
2025-03-06T00:21:00.876+08:00  INFO 13400 --- [           main] org.example.Main                         : No active profile set, falling back to 1 default profile: "default"
2025-03-06T00:21:01.510+08:00  INFO 13400 --- [           main] org.example.Main                         : Started Main in 0.948 seconds (process running for 1.282)
执行切面方法
Result(code: 200, msg: null, data: {"departmentId_text":"Department3","departmentId":3,"name":"test","id":0})
执行切面方法
Result(code: 200, msg: null, data: {"departmentId_text":"Department4","departmentId":4,"name":"test","id":1})
执行切面方法
Result(code: 200, msg: null, data: {"departmentId_text":"Department4","departmentId":4,"name":"test","id":2})
执行切面方法
Result(code: 200, msg: null, data: {"departmentId_text":"Department4","departmentId":4,"name":"test","id":3})
执行切面方法
Result(code: 200, msg: null, data: {"departmentId_text":"Department2","departmentId":2,"name":"test","id":4})
执行切面方法
Result(code: 200, msg: null, data: {"departmentId_text":"Department7","departmentId":7,"name":"test","id":5})
执行切面方法
Result(code: 200, msg: null, data: {"departmentId_text":"Department0","departmentId":0,"name":"test","id":6})
执行切面方法
Result(code: 200, msg: null, data: {"departmentId_text":"Department5","departmentId":5,"name":"test","id":7})
执行切面方法
Result(code: 200, msg: null, data: {"departmentId_text":"Department4","departmentId":4,"name":"test","id":8})
执行切面方法
Result(code: 200, msg: null, data: {"departmentId_text":"Department1","departmentId":1,"name":"test","id":9})

Process finished with exit code 0
```

## 关键特性
- 零侵入性: POJO对象无需实现额外接口或字段。
- 翻译注入: 业务代码仅返回原始数据，翻译逻辑完全由切面代理。
- 动态扩展: 新增字典字段只需添加 @Dict 注解，无需修改切面逻辑。
- 类型安全: Controller层仍返回 Result<User> 类型，前端无需适配代码。