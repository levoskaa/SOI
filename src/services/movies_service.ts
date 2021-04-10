'use strict';

// Import express:
import * as express from 'express';
import { IMovie, IMovieId, IMovieIdList, IMovieList } from '../interfaces/movies';
import { Movie } from '../schemas/movies';

// Create a new express router:
const router = express.Router();

let nextId = 1;

// Create movie
router.post('/', (req, res) => {
    const movieToCreate: IMovie = {
        _id: nextId++,
        ...req.body
    };
    Movie.create(movieToCreate, (err, movie) => {
        if (err) {
            res.json({ info: 'Error executing query.', error: err });
        } else {
            const result: IMovieId = {
                id: movie.id
            };
            res.json(result);
        }
    });
});

// Get movies
router.get('/', (req, res) => {
    Movie.find({}, (err, movies) => {
        if (err) {
            res.json({ info: 'Error executing query.', error: err });
        } else {
            const result: IMovieList = {
                movie: movies
            };
            res.json(result);
        }
    }).select('-__v -_id');
});

// Find movie
router.get('/find', async (req, res) => {
    const year: number = parseInt(req.query.year as string);
    const orderingField: string = req.query.orderby as string;
    if (orderingField.toUpperCase() !== "DIRECTOR" && orderingField.toUpperCase() !== "TITLE") {
        res.sendStatus(400);
    }
    let movies = await Movie.find({ year }).exec();
    if (orderingField.toUpperCase() === "DIRECTOR") {
        movies = movies.sort((a, b) => a.director.localeCompare(b.director));
    } else {
        movies = movies.sort((a, b) => a.title.localeCompare(b.title));
    }
    const result: IMovieIdList = {
        id: movies.map(movie => movie.id)
    };
    res.json(result);
});

// Get movie by id
router.get('/:id', (req, res) => {
    const id: number = parseInt(req.params.id);
    if (id < 0) {
        res.sendStatus(400);
    }
    Movie.findById(id, (err, movie) => {
        if (err) {
            res.json({ info: 'Error executing query.', error: err });
        } else if (movie) {
            const result: IMovie = movie;
            res.json(result);
        } else {
            res.sendStatus(404);
        }
    }).select('-__v -_id');
});

// Upsert movie
router.put('/:id', (req, res) => {
    const id: number = parseInt(req.params.id);
    const movie: IMovie = {
        _id: id,
        ...req.body
    };
    Movie.findByIdAndUpdate(id, movie, { upsert: true, new: true }, (err, movie) => {
        if (err) {
            res.json({ info: 'Error executing query.', error: err });
        } else {
            res.sendStatus(200);
        }
    });
})

// Delete movie
router.delete('/:id', (req, res) => {
    const id: number = parseInt(req.params.id);
    Movie.findByIdAndDelete(id, {}, (err, movie) => {
        if (err) {
            res.json({ info: 'Error executing query.', error: err });
        }
        res.sendStatus(200);
    }).exec();
});

// Export the router:
export default router;
