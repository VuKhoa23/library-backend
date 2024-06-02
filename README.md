| Method | Route  | Input | Output | Error | Description
| ------------- | ------------- | ------------- | ------------- | ------------- | ------------- |
| POST  | /api/rent  | {<br>"userId": 123456789,<br>"bookId": 987654321,<br>"endDate": "2024-06-02"<br>}  | "Book rented successfully!"  | "User not found!" <br> "Not enough book!" <br> "Book not found!" <br> "Librarians are not allowed to access this resource!"  | Only user can rent books  |
