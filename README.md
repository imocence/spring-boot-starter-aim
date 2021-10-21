# Doc API

### 快速文档构建框架

- **基于java注释生成接口文档**
- **注释支持扩展**
- **接口框架支持扩展**
- **默认支持markdown和离线/在线html等格式的文档**
- **默认支持spring mvc规范**
- **默认支持spring-boot直接内嵌启动**


### 1.引入插件方法

- **将项目引入IDEA中maven install后执行如下命令将jar包导入本地仓库中**
  ```
  mvn install:install-file 
    -Dfile=D:\Workfolder\JavaProjects\spring-boot-starter-aim\target\spring-boot-starter-aim-1.0.0.jar 
    -DgroupId=com.aim
    -DartifactId=spring-boot-starter-aim
    -Dversion=1.0.0 -Dpackaging=jar
  ```
- **在需要生成DocApi的springboot项目中添加如下内容**
  ```xml
  <!--加入maven依赖-->
  <dependency>
    <groupId>com.aim</groupId>
    <artifactId>spring-boot-starter-aim</artifactId>
    <version>1.0.0</version>
  </dependency>
  ```
- **在springboot的启动入口中添加@EnableAim**
  ```java
    @EnableAim //<--- 加上此注解以便启用在线HTML文档
    @SpringBootApplication
    public class Application {
    
        public static void main(String[] args) {
            SpringApplication.run(Application.class, args);
        }
    }
  ```
- **在properties或yaml配置文件中添加如下内容,直接在项目里启动时,如果是单模块的maven项目,默认可以不配置**
  ```
    aim.enable=true                                         #是否启动API文档,默认是true,因为可以不填
    aim.sourcePath=src/main/java/com/aim/controller         #源码路径,多个时用英文逗号隔开
    aim.title='用户中心接口文档'                              #用于配置文档页面标题
    aim.version=1.0                                         #标识接口文档的版本号
    aim.reqUrl='http://127.0.0.1:8080/'                     #接口访问地址
    aim.defResult='{"result":{"result":"0","msg":"成功"}}'  #默认返回结果
  或
    aim:
      enable: true
      sourcePath: src/main/java/com/aim/controller
      title: '用户中心接口文档'
      version: '1.0'
      reqUrl: 'http://127.0.0.1:8080/'
      defResult: '{"result":{"result":"0","msg":"成功"}}'
  ```
- **跟着随便写几个Controller作为Demo接口,便于直接浏览生成效果:**
```java
 /**
  * 用户模块
  */
  @Controller
  @RequestMapping("api/user")
  public class UserController {

  /**
    * 登录
    *
    * @param username 用户名|必填
    * @param password 密码
    * @return 当前登录用户的基本信息
    * @resp code 返回码(0000表示登录成功,其它表示失败)|string|必填
    * @resp msg 登录信息|string
    * @resp username 登录成功后返回的用户名|string
      */
      @ResponseBody
      @PostMapping("login")
      public Map<String, String> login(String username, String password) {
      Map<String, String> model = new HashMap<>();
      model.put("code", "0000");
      model.put("msg", "登录成功");
      model.put("username", username);
      return model;
      }


    /**
     * 用户注册
     *
     * @param user :username 用户名|必填
     * @param user :password 密码
     * @return 注册后生成的用户的基本信息
     * @respbody {"id":"123","password":"123456","username":"admin"}
     * @title 注册
     * @see User
     * @resp score 分数
     */
    @ResponseBody
    @RequestMapping(value = "register", method = {RequestMethod.POST, RequestMethod.PUT})
    User register(User user, @RequestParam(value = "abc", required = false)List<MultipartFile> list) {
        user.setId(UUID.randomUUID().toString());
        return user;
    }
}
```
- **直接启动项目, 敲入地址: http://localhost:8080/aim**

### 2.如果想生成离线文档怎么办?
**支持html:生成后可以在自己的controller当中添加访问路径**
```java
@Test
public void buildHtml() throws Exception {
    //生成离线的HTML格式的接口文档
    String userDir = System.getProperty("user.dir");
    FileOutputStream out = new FileOutputStream(new File(userDir, "api.html"));
    AiMain aim = new AiMain(userDir + "/src/main/java/com/aim/controller", new SpringWebFramework());
    aim.build(out, new HtmlForamt());
}
```

**也支持markdown:**
```java
@Test
public void buildMarkdown() {
    //生成离线的Markdown格式的接口文档
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    String rootDir = System.getProperty("user.dir");
    AiMain aim = new AiMain(rootDir + "/src/main/java/com/aim/controller", new SpringWebFramework());
    aim.build(out, new MarkdownFormat());

    System.out.println(out.toString());
}
```

**tip:生产环境不推荐开启此文档,所以配置文件中配置好:**
```txt
aim.enable=false
```