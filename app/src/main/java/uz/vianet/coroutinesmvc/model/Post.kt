package uz.vianet.coroutinesmvc.model

 class Post {
    var id: Int = 0
    var userId: Int = 0
    var title: String
    var body: String

    constructor(userId: Int, title: String, body: String) {
        this.userId = userId
        this.title = title
        this.body = body
    }

    constructor(id: Int, userId: Int, title: String, body: String) {
        this.id = id
        this.userId = userId
        this.title = title
        this.body = body
    }
}