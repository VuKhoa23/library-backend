## Renting API
| Method | Route  | Input | Output | Error | Description
| ------------- | ------------- | ------------- | ------------- | ------------- | ------------- |
| POST  | /api/rent  | {<br>"userId": Long,<br>"bookId": Long,<br>"endDate": Date<br>}  | "Book rented successfully!" <br> - *HTTP Status: 200*  | "User not found!" - *HTTP Status: 400* <br> "Not enough book! - *HTTP Status: 400*" <br> "Book not found!" - *HTTP Status: 400* <br> "Librarians are not allowed to access this resource!" - *HTTP Status: 403*  | Only user can rent books  |
