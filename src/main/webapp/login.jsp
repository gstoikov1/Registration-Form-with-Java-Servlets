<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login Form</title>
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
    <h2>Login</h2>
    <form action="login" method="POST">
        <label for="username">Username</label>
        <input type="email" id="email" name="email" required>
        
        <label for="password">Password</label>
        <input type="password" id="password" name="password" required>
        
        <button type="submit">Login</button>
        <p><a href="register.jsp">Register</a></p>
        
    </form>
     <div class="status">
        <% 
            String status = (String) request.getAttribute("status");
            if (status != null) {
                if (status.equals("error")) {
                    out.println("Registration failed. Please try again.");
                } else if (status.equals("wrong_data")) {
                	out.println("Wrong combination of email and password");
                }
                request.removeAttribute("status");
            }
        %>
    </div>
</div>
</body>
</html>
