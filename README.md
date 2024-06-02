## Renting API
| Method | Route  | Input | Output _____________________| Error | Description
| ------------- | ------------- | ------------- | ------------- | ------------- | ------------- |
| POST  | /api/rent  | {<br>"userId": Long,<br>"bookId": Long,<br>"endDate": Date<br>}  | "Book rented successfully!" <br> - *HTTP Status: 200*  | "User not found!" - *HTTP Status: 400* <br> "Not enough book! - *HTTP Status: 400*" <br> "Book not found!" - *HTTP Status: 400* <br> "Librarians are not allowed to access this resource!" - *HTTP Status: 403*  | Only user can rent books  |
## Auth API
| Method | Route  | Input | Output __________________ | Error | Description
| ------------- | ------------- | ------------- | ------------- | ------------- | ------------- |
| POST  | /api/auth/register  | {<br>"username": String<br>"password": String <br>}  | "User registered" <br> - *HTTP Status: 200*  | "Username is already taken" - *HTTP Status: 400* <br> | User register |
| POST  | /api/auth/login  | {<br>"username": String<br>"password": String <br>}  | {<br>"accessToken": Token <br> "tokenType": "Bearer " <br> "message": "Success" <br>} - *HTTP Status: 200*  |  {<br>"message": "Bad credentials" <br> } - *HTTP Status: 400* | User login |
