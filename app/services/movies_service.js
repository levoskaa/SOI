'use strict';
Object.defineProperty(exports, "__esModule", { value: true });
const express = require("express");
const movies_1 = require("../schemas/movies");
const router = express.Router();
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
exports.default = router;
//# sourceMappingURL=movies_service.js.map