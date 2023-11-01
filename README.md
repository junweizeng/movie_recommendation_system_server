# movie_recommendation_system_server

Spring Boot + Spring Security + Redis + MyBatis-Plus

## 0. 电影推荐系统前端

前端GitHub地址：[movie_recommendation_system_vue](https://github.com/Vanish-Zeng/movie_recommendation_system_vue)

## 1. 项目打包部署

### 1.1 项目打包

IDEA右边栏中选择`Maven` → 按住`Ctrl` → 选择`Lifecycle`下的`clean`和`package` → 点击上方的`绿色运行按钮` → 等待项目打包成jar包 → 打包好的jar包会在项目的`target`目录中

![项目打成jar包.png](README_IMG/项目打成jar包.png)

### 1.2 项目部署

1. 将打包好的`项目jar包`上传到自己的服务器上。

2. 通过以下命令时项目在服务器后台运行，并且输出日志到`out.txt`文件（可修改）：
    
    >nohup java -jar 项目名.jar >out.txt &

## 2. 功能实现

1. 用户登录、注册、个人信息修改等接口实现。
2. 基于内容推荐和基于用户协同过滤推荐等推荐算法实现，整合两种算法实现混合式推荐，并解决冷启动问题。
3. 查询各种电影信息接口实现（如电影搜索、推荐给用户的电影信息查询等）。
4. 用户点赞功接口实现（点赞信息暂存，再按时持久化到数据库）。
5. ...
