import pymysql
import csv

# Connection
con = pymysql.connect(host='localhost',
                      user='root',
                      password='5749',
                      db='bike',
                      charset='utf8')

cursorObject = con.cursor()

print("connect successful!!")

# SQL query string
sqlQuery = ("select * from BikeRental where cnt > 5000")

# Execute the sqlQuery
cursorObject.execute(sqlQuery)

# Fetch all the rows
rows = cursorObject.fetchall()

for row in rows:
    print(row)
    #print(row[0], ",", row[1], ",", row[2])

# Write to CSV file
f = open('mysql_output.csv', 'w', encoding='utf-8', newline='')
wr = csv.writer(f)

for row in rows:
    wr.writerow(row)
f.close()

# Close connection
con.commit()
con.close()

