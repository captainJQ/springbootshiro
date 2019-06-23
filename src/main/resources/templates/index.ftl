<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>index</title>
</head>
<body>
    <form method="post" action="/logout">
        <input type="submit" value="注销">
    </form>
    <@shiro.hasRole name="ADMIN">
        ADMIN<BR>
    </@shiro.hasRole>
    <@shiro.hasRole name="USER">
        USER<br>
    </@shiro.hasRole>
    <@shiro.hasRole name="ROOT">
        ROOT<br>
    </@shiro.hasRole>
    <@shiro.hasPermission name="ADMIN:USER:UPDATE">
        UPDATE<br>
    </@shiro.hasPermission>
    <@shiro.hasPermission name="ADMIN:USER:DELETE">
        DELETE<br>
    </@shiro.hasPermission>
    <@shiro.hasPermission name="ADMIN:USER:ADD">
        ADD<br>
    </@shiro.hasPermission>

</body>
</html>