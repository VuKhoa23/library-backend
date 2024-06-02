| Method | Route  | Input | Output | Error | Description
| ------------- | ------------- | ------------- | ------------- | ------------- | ------------- |
| POST  | /api/rent  | {<br>"userId": Long,<br>"bookId": Long,<br>"endDate": Date<br>}  | "Book rented successfully!" <br>*HTTP Status: 200*  | "User not found!" <br> "Not enough book!" <br> "Book not found!" <br> "Librarians are not allowed to access this resource!" <br>*HTTP Status: 400*  | Only user can rent books  |
