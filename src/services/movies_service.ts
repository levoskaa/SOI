'use strict';

// Import express:
import * as express from 'express';
import { Movie } from '../schemas/movies';

// Create a new express router:
const router = express.Router();

// Get movies
router.get('/', (req, res) => {
    // TODO
    Movie.find({}, (err, movies) => {
        if (err) {
            res.json({ info: 'Error executing query.', error: err });
        } else {
            res.json({ movie: movies });
        }
    })
});

// Export the router:
export default router;
