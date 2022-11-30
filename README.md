![foto](https://i.imgur.com/1UT5EuC.png)
[Imgur](https://imgur.com/HnGozRq)
# AttendanceApp
an Attendance application created with kotlin, API based on Supabase 

## Application Implementation

### 😎 Application Features
∙ Daily Attendance Detection <br>
∙ Current Location where you've Checked-In and Checked-Out<br>
∙ Send Pictures when you're Checking-In ( You can Save them Pictures or Copy the URL )

### 📦 Database - Using the Supabase API
∙ To store Attendance Pictures and Profile Pictures I use **Imgur API** <br>
∙ Daily Attendance Data Detection uses the ``"date eq.now()"`` query on Params <br>
∙ The database also stores where user **Checked-Out** and **Checked-In** Location by **Longtitude** and **Latitude**

