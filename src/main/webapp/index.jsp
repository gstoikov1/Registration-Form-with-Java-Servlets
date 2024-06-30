<%
    if (session.getAttribute("name") == null) {
        response.sendRedirect("register.jsp"); 
    } else {
        String username = (String) session.getAttribute("name"); 
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome <%= username %></title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f0f0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .form-container {
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            width: 300px; 
            text-align: center;
        }
        .form-container h2 {
            margin-bottom: 20px;
        }
        .form-container button {
            width: 100%;
            padding: 10px;
            background-color: #5cb85c;
            border: none;
            border-radius: 4px;
            color: #fff;
            font-size: 16px;
            margin-bottom: 10px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        .form-container button:hover {
            background-color: #4cae4c;
        }
    </style>
</head>
<body>

<div class="form-container">
    <h2>Welcome, <%= username %></h2>
    <form action="change_data/change_info.jsp">
        <button type="submit">Change Info</button>
    </form>
  <form action="logout.jsp">
        <button type="submit">Log Out</button>
    </form>
    
</div>

</body>
</html>

<%
    }
%>
