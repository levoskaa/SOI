'use strict';

// Import express:
import * as express from 'express';
import { Movie } from '../schemas/movies';

// Create a new express router:
const router = express.Router();

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
