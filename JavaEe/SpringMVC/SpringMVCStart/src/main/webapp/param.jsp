<%--
  Created by IntelliJ IDEA.
  User: MECHREVO
  Date: 2020/6/22
  Time: 22:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h3>请求参数绑定</h3>

    <a href="/param/testParam?username=hehe&password=123456">请求参数绑定</a>


    <%--封装集合和类--%>
    <form action="/param/saveAccount" method="post">
        姓名：<input type="text" name="username" /><br/>
        密码：<input type="text" name="password" /><br/>
        金额：<input type="text" name="account" /><br/>
        用户姓名：<input type="text" name="list[0].name" /><br/>
        用户年龄：<input type="text" name="list[0].age" /><br/>
        用户姓名：<input type="text" name="map['one'].name" /><br/>
        用户年龄：<input type="text" name="map['one'].age" /><br/>
<%--        用户姓名：<input type="text" name="user.name" /><br/>--%>
<%--        用户年龄：<input type="text" name="user.age" /><br/>--%>
        <input type="submit" name="提交" /><br/>
    </form>

    <%--封装集合和类--%>
    <form action="/param/saveUser" method="post">
        用户姓名：<input type="text" name="uname" /><br/>
        用户年龄：<input type="text" name="uage" /><br/>
        用户生日：<input type="text" name="date" /><br/>

        <input type="submit" name="提交" /><br/>
    </form>
</body>
</html>
