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
            const result = {
                id: movie.id
            };
            res.json(result);
        }
    });
});
router.get('/', (req, res) => {
    movies_1.Movie.find({}, (err, movies) => {
        if (err) {
            res.json({ info: 'Error executing query.', error: err });
        }
        else {
            const result = {
                movie: movies
            };
            res.json(result);
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
            const result = movie;
            res.json(result);
        }
        else {
            res.sendStatus(404);
        }
    });
});
exports.default = router;
//# sourceMappingURL=movies_service.js.map