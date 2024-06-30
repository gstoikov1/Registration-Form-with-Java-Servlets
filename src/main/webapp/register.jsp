<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register Form</title>
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
        }
        .form-container h2 {
            margin-bottom: 20px;
        }
        .form-container label {
            display: block;
            margin-bottom: 8px;
        }
        .form-container input {
            width: 100%;
            padding: 8px;
            margin-bottom: 12px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        .form-container button {
            width: 100%;
            padding: 10px;
            background-color: #5cb85c;
            border: none;
            border-radius: 4px;
            color: #fff;
            font-size: 16px;
        }
        .form-container button:hover {
            background-color: #4cae4c;
        }
    </style>
</head>
<body>

<div class="form-container">
    <h2>Register</h2>
    <form action="register" method="POST">
        <label for="username">Username</label>
        <input type="text" id="username" name="username" required>
        
        <label for="email">Email</label>
        <input type="email" id="email" name="email" required>
        
        <label for="password">Password</label>
        <input type="password" id="password" name="password" required>
        
        <label for="age">Age</label>
        <input type="text" id="age" name="age" required>
        <button type="submit">Register</button>
      	 <p><a href="login.jsp">Log in</a></p>
    </form>
     <div class="status">
        <% 
            String status = (String) request.getAttribute("status");
            if (status != null) {
                if (status.equals("failure_username_taken")) {
                    out.println("Registration unsuccessful. Username is in use");
                } else if (status.equals("failure_email_taken")) {
                    out.println("Registration unsuccessful. Email is in use");
                }
                request.removeAttribute("status");
            }
        %>
    </div>
</div>

</body>
</html>
