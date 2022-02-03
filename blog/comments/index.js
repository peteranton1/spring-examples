const express = require('express');
const bodyParser = require('body-parser');
const { randomBytes } = require('crypto');
const cors = require('cors');
const axios = require('axios');

const app = express();
app.use(bodyParser.json());
app.use(cors());

const commentsByPostId = {};

app.get('/posts/:id/comments', (req, res) => {
    const comments = commentsByPostId[req.params.id] || [];
    res.send(comments);
});

app.post('/posts/:id/comments', async (req, res) => {
    const commentId = randomBytes(4).toString('hex');
    const { content } = req.body;
    const postId = req.params.id;
    const comments = commentsByPostId[postId] || [];

    comments.push({ id: commentId, content: content });

    commentsByPostId[postId] = comments;

    await axios.post('http://localhost:4005/events', {
        type: 'CommentCreated',
        data: {
            id: commentId, 
            content, 
            postId: postId
        }
    }).catch((err) => {
        console.log(err.message);
    });

    res.status(201).send(comments);
});

app.post('/events', (req, res) => {
    console.log('Received Event: ' + req.body.type);
});

app.listen(4001, () => {
    console.log("Listening on 4001");
});

