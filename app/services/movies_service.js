'use strict';
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
Object.defineProperty(exports, "__esModule", { value: true });
const express = require("express");
const movies_1 = require("../schemas/movies");
const router = express.Router();
let nextId = 1;
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
    }).select('-__v -_id');
});
router.get('/find', (req, res) => __awaiter(void 0, void 0, void 0, function* () {
    const year = parseInt(req.query.year);
    const orderingField = req.query.orderby;
    if (orderingField.toUpperCase() !== "DIRECTOR" && orderingField.toUpperCase() !== "TITLE") {
        res.sendStatus(400);
    }
    let movies = yield movies_1.Movie.find({ year }).exec();
    if (orderingField.toUpperCase() === "DIRECTOR") {
        movies = movies.sort((a, b) => a.director.localeCompare(b.director));
    }
    else {
        movies = movies.sort((a, b) => a.title.localeCompare(b.title));
    }
    const result = {
        id: movies.map(movie => movie.id)
    };
    res.json(result);
}));
router.get('/:id', (req, res) => {
    const id = parseInt(req.params.id);
    if (id < 0) {
        res.sendStatus(400);
    }
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
    }).select('-__v -_id');
});
router.put('/:id', (req, res) => {
    const id = parseInt(req.params.id);
    const movie = Object.assign({ _id: id }, req.body);
    movies_1.Movie.findByIdAndUpdate(id, movie, { upsert: true, new: true }, (err, movie) => {
        if (err) {
            res.json({ info: 'Error executing query.', error: err });
        }
        else {
            res.sendStatus(200);
        }
    });
});
router.delete('/:id', (req, res) => {
    const id = parseInt(req.params.id);
    movies_1.Movie.findByIdAndDelete(id, {}, (err, movie) => {
        if (err) {
            res.json({ info: 'Error executing query.', error: err });
        }
        res.sendStatus(200);
    }).exec();
});
exports.default = router;
//# sourceMappingURL=movies_service.js.map