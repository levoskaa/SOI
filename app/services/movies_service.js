'use strict';
Object.defineProperty(exports, "__esModule", { value: true });
const express = require("express");
const movies_1 = require("../schemas/movies");
const router = express.Router();
let nextId = 0;
router.post('/', (req, res) => {
    const movieToCreate = Object.assign({ _id: nextId++ }, req.body);
    movies_1.Movie.create(movieToCreate, (err, movie) => {
        if (err) {
            res.json({ info: 'Error executing query.', error: err });
        }
        else {
            res.json({ id: movie.id });
        }
    });
});
router.get('/', (req, res) => {
    movies_1.Movie.find({}, (err, movies) => {
        if (err) {
            res.json({ info: 'Error executing query.', error: err });
        }
        else {
            res.json({ movie: movies });
        }
    });
});
router.get('/:id', (req, res) => {
    const id = req.params.id;
    movies_1.Movie.findById(id, (err, movie) => {
        if (err) {
            res.json({ info: 'Error executing query.', error: err });
        }
        else if (movie) {
            res.json(movie);
        }
        else {
            res.sendStatus(404);
        }
    });
});
exports.default = router;
//# sourceMappingURL=movies_service.js.map