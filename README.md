<img alt="Banner" src="https://raw.githubusercontent.com/akimov1712/YumlyApi/refs/heads/release/.github/workflows/banner.png">

# Yumly API

**Yumly API** is a backend API for a mobile culinary application with an integrated AI assistant.  
It allows users to:  

- Browse and search recipes  
- Manage personal profiles  
- Save favorite recipes  
- Follow other users  
- Interact with an AI assistant for cooking advice  
- Upload images for recipes and profiles  
- Receive notifications for likes and followers


The API is designed using Kotlin, Ktor, JWT authentication, and MySQL.



## ⚙️ Environment Setup

Create a `.env` file in the root directory:

```env
DATABASE_URL=jdbc:mysql://localhost:3306/yumly
DATABASE_USER=root
DATABASE_PASSWORD=password
DATABASE_DRIVER=com.mysql.cj.jdbc.Driver

JWT_SECRET=your_jwt_secret
JWT_ISSUER=yumly
JWT_AUDIENCE=yumly_audience
JWT_REALM=Yumly API
JWT_KEY_EMAIL=email

SENDER_EMAIL=your_email@gmail.com
SENDER_APP_PASSWORD=app_password

GPT_ID_FOLDER=id_folder
GPT_API_KEY=api_key
GPT_SYSTEM_ROLE_TEXT=system_role
```

---

## 📖 API documentation

All data is sent and received in **JSON** format.

**Authentication**: all secure endpoints require JWT:


```makefile
Authorization: Bearer <token>
```

**Authentication legend:**

- 🔑🟢 — JWT required

- 🔑🔴 — JWT optional

Optional or nullable fields are marked with **(optional, nullable)**.

## 1. Authentication & Registration

### 1.1 Sign Up
**POST /v1/signUp 🔑🔴**

**Request:**
```json
{
  "email": "user@example.com",
  "username": "chef123",
  "password": "password123",
  "photoUrl": "https://example.com/photo.jpg"  // optional, nullable
}
```

**Response:**
```json
{
  "id": 1,
  "username": "chef123",
  "email": "user@example.com",
  "photoUrl": "https://example.com/photo.jpg",  // optional, nullable
  "isVerified": false,
  "createdAt": "2025-09-16T12:34:56",
  "updatedAt": "2025-09-16T12:34:56"
}
```

**Errors** 
- `409 CONFLICT` — user already exists
- `400 BAD_REQUEST` — invalid email, username too short, or password too short

ㅤ
ㅤ

### 1.2 Login
**POST /v1/login 🔑🔴**

**Request:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}

```

**Response:**
```json
{
  "token": "jwt-token"
}

```

**Errors** 
- `404 NOT_FOUND` — user not found or incorrect password

ㅤ
ㅤ

### 1.3 Request Verification Code
**POST /v1/verify/request 🔑🔴**

**Request:**
```json
{
  "email": "user@example.com"
}

```

**Response:** `200 OK`

**Errors** 
- `404 NOT_FOUND` — user not found

ㅤ
ㅤ

### 1.4 Confirm Account
**POST /v1/verify/confirm 🔑🔴**

**Request:**
```json
{
  "email": "user@example.com",
  "code": "123456"
}

```

**Response:**
```json
{
  "token": "jwt-token"
}

```

**Errors** 
- `403 FORBIDDEN` — code expired or incorrect

ㅤ
ㅤ

### 1.5 Password Reset Request
**POST /v1/reset/request 🔑🔴**

**Request:**
```json
{
  "email": "user@example.com"
}

```

**Response:** `200 OK`

**Errors** 
- `404 NOT_FOUND` — user not found
ㅤ

ㅤ
ㅤ
### 1.6 Password Reset Confirm
**POST /v1/reset/confirm 🔑🔴**

**Request:**
```json
{
  "email": "user@example.com",
  "code": "123456",
  "newPassword": "newPassword123"
}
```

**Response:**
```json
{
  "status": "SUCCESS"
}
```

**Errors** 
- `403 FORBIDDEN` — code expired or does not match

ㅤ
## 2. User Account
### 2.1 Get Own Account Info
**GET /v1/account/info 🔑🟢**

**Response:**
```json
{
  "id": 1,
  "username": "chef123",
  "email": "user@example.com",
  "photoUrl": "https://example.com/photo.jpg",  // optional, nullable
  "isVerified": true,
  "createdAt": "2025-09-16T12:34:56",
  "updatedAt": "2025-09-16T12:34:56"
}

```

**Errors**
- `401 UNAUTHORIZED` — JWT missing or invalid
ㅤ

ㅤㅤ
ㅤ
### 2.2 Update Own Account Info
**PUT /v1/account/info 🔑🟢**

**Request:**
```json
{
  "username": "newUsername",       // optional, nullable
  "photoUrl": "https://example.com/newphoto.jpg"  // optional, nullable
}

```

**Response:**
```json
{
  "id": 1,
  "username": "chef123",
  "email": "user@example.com",
  "photoUrl": "https://example.com/photo.jpg",  // optional, nullable
  "isVerified": false,
  "createdAt": "2025-09-16T12:34:56",
  "updatedAt": "2025-09-16T12:34:56"
}

```

**Errors**
- `400 BAD_REQUEST` — username too short (<4 characters)
ㅤ

ㅤㅤ
ㅤ
### 2.3 Get Another User Profile
**GET /v1/profile/info/{id} 🔑🔴**

**Response:**
```json
{
  "userId": 2,
  "username": "chef2",
  "email": "chef2@example.com",         // optional, nullable
  "photoUrl": "https://example.com/photo2.jpg",  // optional, nullable
  "countFollowing": 10,
  "countFollowers": 20,
  "countLikes": 50
}

```

**Errors**
- `400 BAD_REQUEST` — invalid ID
- `404 NOT_FOUND` — user not found
ㅤ

ㅤㅤ
ㅤ
## 3. Recipes
### 3.1 Get Recipes
**POST /v1/recipe 🔑🔴**

**Request:**
```json
{
  "q": "pasta",              // optional, nullable
  "offset": 0,               // optional, default 0
  "limit": 20,               // optional, default 20
  "recipeFilter": {          // optional, nullable
    "tagIds": [1,2],         // optional, nullable
    "cookingTime": 60,        // optional, nullable
    "minKcal": 100,           // optional, nullable
    "maxKcal": 500,           // optional, nullable
    "difficulty": "Easy"      // optional, nullable
  }
}

```

**Response:**
```json
[
	{
	"id": 228,
	"author": {
		"userId": 3,
		"username": "Alexey Zimin",
		"email": "eda191559@mail.ru",
		"photoUrl": "url",
		"countFollowing": 0,
		"countFollowers": 0,
		"countLikes": 0
	},
	"title": "Genoese pie with ricotta and zucchini",
	"description": "",
	"largeImage": "url",
	"smallImage": "url",
	"isFavorite": false,
	"difficulty": "Hard",
	"cookingTime": 40,
	"kcal": 672,
	"protein": 25.0,
	"fat": 47.0,
	"carb": 39.0,
	"tags": [],
	"ingredients": [
		{
			"id": 2236,
			"name": "Zucchini",
			"value": "1 kg"
		}
	],
	"steps": [
		{
			"id": 1446,
			"description": "Cut two zucchini into half-rings and fry until soft and lightly golden in olive oil with a knob of butter. Cut the remaining zucchini into thin slices (these will be used for garnish) and set aside.",
			"previewUrl": "url"
		}
	]
}
]
```

**Errors**
- `400 BAD_REQUEST` — invalid parameters
ㅤ

ㅤㅤ
ㅤ
### 3.2 Get Recipe By ID
**GET /v1/recipe/{id} 🔑🔴**

**Response:**
```json
	{
	"id": 228,
	"author": {
		"userId": 3,
		"username": "Alexey Zimin",
		"email": "eda191559@mail.ru",
		"photoUrl": "url",
		"countFollowing": 0,
		"countFollowers": 0,
		"countLikes": 0
	},
	"title": "Genoese pie with ricotta and zucchini",
	"description": "",
	"largeImage": "url",
	"smallImage": "url",
	"isFavorite": false,
	"difficulty": "Hard",
	"cookingTime": 40,
	"kcal": 672,
	"protein": 25.0,
	"fat": 47.0,
	"carb": 39.0,
	"tags": [],
	"ingredients": [
		{
			"id": 2236,
			"name": "Zucchini",
			"value": "1 kg"
		}
	],
	"steps": [
		{
			"id": 1446,
			"description": "Cut two zucchini into half-rings and fry until soft and lightly golden in olive oil with a knob of butter. Cut the remaining zucchini into thin slices (these will be used for garnish) and set aside.",
			"previewUrl": "url"
		}
	]
}
```

**Errors**
- `400 BAD_REQUEST` — invalid ID
- `404 NOT_FOUND` — recipe not found

ㅤ

ㅤㅤ
ㅤ
### 3.3 Add New Recipe
**POST /v1/recipe/add 🔑🟢**

**Request:**
```json
{
  "title": "Pasta with Sauce",
  "description": "Easy recipe",              // optional, nullable
  "previewUrl": "url", // optional, nullable
  "cookingTime": 30,
  "kcal": 400,
  "protein": 20,
  "fat": 10,
  "carb": 50,
  "ingredients": [{"name": "Pasta", "value": "200g"}],
  "steps": [{"description": "Boil pasta", "previewUrl": null}], // optional, nullable
  "tagIds": []   // optional, nullable
}
```

**Response:**
```json
	"id": 228,
	"author": {
		"userId": id,
		"username": "",
		"email": "",
		"photoUrl": "",
		"countFollowing": 0,
		"countFollowers": 0,
		"countLikes": 0
	},
	"title": "Pasta with Sauce",
	"description": "Easy recipe",
	"largeImage": "url",
	"smallImage": "url",
	"isFavorite": false,
	"difficulty": "Hard",
	"cookingTime": 30,
  "kcal": 400,
  "protein": 20,
  "fat": 10,
  "carb": 50,
	"carb": 39.0,
	"tags": [],
	"ingredients": [
		{
			"id": 0,
			"name": "Pasta",
			"value": "200g"
		}
	],
	"steps": [
		{
			"id": 9,
			"description": "Boil pasta",
			"previewUrl": null
		}
	]
}
```

**Errors**
- `409 CONFLICT` — validation errors
- `401 UNAUTHORIZED` — JWT missing

ㅤ

ㅤㅤ
ㅤ
### 3.4 Delete Recipe
**DELETE /v1/recipe/{id} 🔑🟢**

**Response:** `200 OK`

**Errors**
- `400 BAD_REQUEST` — invalid ID
- `403 FORBIDDEN` — user is not author
- `404 NOT_FOUND` — recipe not found
ㅤ

ㅤㅤ
ㅤ
## 4. Favorites
### 4.1 Switch Favorite
**POST /v1/favorite/{id} 🔑🟢**

**Response: `true` if added, `false` if removed

**Errors**
- `400 BAD_REQUEST` — invalid ID
- `404 NOT_FOUND` — recipe not found

ㅤ

ㅤㅤ
ㅤ
### 4.2 Get My Favorites
**POST /v1/favorite/my 🔑🟢**

**Request:**
```json
{
  "limit": 20,      // optional, default 20
  "offset": 0       // optional, default 0
}
```

**Response:** List of favorite recipes
ㅤ

ㅤㅤ
ㅤ
### 4.3 Get Another User's Favorites
**GET /v1/favorite/{id} 🔑🔴**

**Request:**
```json
{
  "limit": 20,      // optional, default 20
  "offset": 0       // optional, default 0
}
```

**Response:** List of recipes

ㅤ

ㅤㅤ
## 5. Follow System
### 5.1 Switch Follow Status
**POST /v1/follow/{id} 🔑🟢**

**Response:** `true` if followed, `false` if unfollowed

**Errors**
- `400 BAD_REQUEST` — invalid ID or self-follow
- `404 NOT_FOUND` — user not found

ㅤ

ㅤㅤ
ㅤ
### 5.2 Get Followers
**POST /v1/follow/followers 🔑🔴**

**Request:**
```json
{
  "followId": 2,
  "limit": 20,   // optional, default 20
  "offset": 0    // optional, default 0
}

```

**Response:**
```json
{
  "count": 15,
  "follows": [
    {
      "id": 1,
      "username": "chef1",
      "email": "chef1@example.com",   // optional, nullable
      "photoUrl": null                // optional, nullable
    }
  ]
}

```

ㅤ

ㅤㅤ
ㅤ
### 5.3 Get Following
**POST /v1/follow/following 🔑🔴**

**Request:**
```json
{
  "followId": 2,
  "limit": 20,   // optional, default 20
  "offset": 0    // optional, default 0
}

```

**Response:**
```json
{
  "count": 15,
  "follows": [
    {
      "id": 1,
      "username": "chef1",
      "email": "chef1@example.com",   // optional, nullable
      "photoUrl": null                // optional, nullable
    }
  ]
}
```

ㅤ

ㅤㅤ
## 6. Notifications
### 6.1 Get Notifications
**POST /v1/notification 🔑🟢**

**Request:**
```json
{
  "limit": 20,   // optional, default 20
  "offset": 0    // optional, default 0
}

```

**Response:**
```json
{
  "id": 0,,
  "type": "FOLLOW",
  "initiator": {
    `user`
  },
  "recipe": null,
  "createdAt": `date`
}
```

ㅤ

ㅤㅤ
ㅤ
## 7. GPT Chat
### 7.1 Get All Chats
**POST /v1/gpt 🔑🟢**

**Request:**
```json
{
  "offset": 0,   // optional, default 0
  "limit": 20    // optional, default 20
}
```

**Response:**
```json
[
  {
    "id": 0,
    "userId": 0,
    "messages": {
      "id": 0,
      "role": "ASSISTANT",
      "text": "text".
      "createdAt": `date`
    },
    "createdAt": `date`
  }
]
```


ㅤ

ㅤㅤ
ㅤ
### 7.2 Get Chat By ID
**GET /v1/gpt/{id} 🔑🟢**

**Response:**
```json
{
  "id": 0,
  "userId": 0,
  "messages": {
    "id": 0,
    "role": "ASSISTANT",
    "text": "text".
    "createdAt": `date`
  },
  "createdAt": `date`
}
```

**Errors**
- `400 BAD_REQUEST` — invalid ID
- `404 NOT_FOUND` — chat not found
- `403 FORBIDDEN` — chat belongs to another user


ㅤ

ㅤㅤ
ㅤ
### 7.3 Send Message
**POST /v1/gpt/send 🔑🟢**

**Request:**
```json
{
  "chatId": 1,             // optional, nullable; if null a new chat is created
  "text": "How to cook pasta?"
}
```

**Response:** Updated chat with assistant reply

**Errors**
- `400 BAD_REQUEST` — GPT API request failed



ㅤ

ㅤㅤ
## 8. History
### 8.1 Get Top Queries
**GET /v1/history/top 🔑🔴**

**Response:**
```json
[
  "pasta",
  "chocolate cake",
  "salad"
]
```


ㅤ

ㅤㅤ
ㅤ
## 9. Image Upload
### 9.1 Upload Image
**POST /v1/upload 🔑🟢**

**Request:** Multipart form-data, file under `file` key

**Response:**
```json
{
  "url": "drawable/unique_filename.jpg"
}

```

**Errors**
- `400 BAD_REQUEST` — invalid file type, size > 8MB, or no file uploaded

**Static File Access**
- `GET /v1/drawable/{filename}` — serves uploaded images


## 💡 Development plans
- Docker Compose support for quick deployment
- Web admin panel for managing recipes and users
- AI assistant extension for generating recipes based on ingredients
- Adding ratings and comments to recipes


## 🔒 License

```
Proprietary License

Copyright (c) 2025 akimov1712. 
All rights reserved.

This software is **proprietary** and its use is strictly limited.  

You are NOT allowed to:
- Use, copy, modify, or distribute this software without express written permission from the copyright holder.
- Incorporate this software into other projects, products, or services without authorization.
- Share or sell this software in any form.

You are allowed to:
- Use this software only if you have received explicit permission from the copyright holder.

Unauthorized use may result in legal action.
```
ㅤ
