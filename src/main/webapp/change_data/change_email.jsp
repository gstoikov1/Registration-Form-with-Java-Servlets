<%
    if (session.getAttribute("name") == null) {
        response.sendRedirect("register.jsp"); 
    } else {
        String username = (String) session.getAttribute("name"); 
        String email = (String) session.getAttribute("email");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Change Details</title>
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
        .form-container label {
            display: block;
            text-align: left;
            margin-bottom: 8px;
        }
        .form-container input {
            width: 100%;
            padding: 8px;
            margin-bottom: 12px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }
        .form-container button {
            width: 100%;
            padding: 10px;
            background-color: #5cb85c;
            border: none;
            border-radius: 4px;
            color: #fff;
            font-size: 16px;
            margin-top: 10px;
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
    <h2>Change Details</h2>
    <form action="updateEmail" method="POST">
        <label for="newEmail">Email :<%=email%></label>
        <input type="text" id="newEmail" name="newEmail" required>
        <label for="password">Current Password</label>
        <input type="text" id="password" name = "password" required>
        <button type="submit">Change Email</button>
    </form>
    <form action="change_info.jsp">
        <button type="submit">Back</button>
    </form>
      <div class="status">
        <% 
            String status = (String) request.getAttribute("status");
            if (status != null) {
                if (status.equals("failure_email_taken")) {
                    out.println("Email is already in use");
                } else if (status.equals("failure_wrong_password")) {
                    out.println("Wrong Password");
                } else if (status.equals("failure")) {
                    out.println("Unknown error");
                } else if(status.equals("success")) {
                	out.println("Email changed successfully");
                }
                request.removeAttribute("status");
            }
        %>
    </div>
   
</div>

</body>
</html>

<%
    }
%>
