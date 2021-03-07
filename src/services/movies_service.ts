'use strict';

// Import express:
import * as express from 'express';
import { Movie, MovieEntity } from '../schemas/movies';

// Create a new express router:
const router = express.Router();

let nextId = 0;

// Create movie
router.post('/', (req, res) => {
    const movieToCreate = {
        _id: nextId++,
        ...req.body
    }
    Movie.create(movieToCreate, (err, movie) => {
        if (err) {
            res.json({ info: 'Error executing query.', error: err });
        } else {
            res.json({ id: movie.id });
        }
    })
});

// Get movies
router.get('/', (req, res) => {
    Movie.find({}, (err, movies) => {
        if (err) {
            res.json({ info: 'Error executing query.', error: err });
        } else {
            res.json({ movie: movies });
        }
    })
});

// Get movie by id
router.get('/:id', (req, res) => {
    const id = req.params.id;
    Movie.findById(id, (err, movie) => {
        if (err) {
            res.sendStatus(404);
        } else {
            res.json(movie);
        }
    });
});

// Export the router:
export default router;
