'use_strict';

import * as mongoose from 'mongoose';
import { IMovie } from '../interfaces/movies';

export interface MovieEntity extends IMovie, mongoose.Document { }

let MovieSchema = new mongoose.Schema({
    _id: {
        type: Number,
        select: false
    },
    __v: {
        type: Number,
        select: false
    },
    title: String,
    year: Number,
    director: String,
    actor: [String]
});

export var Movie = mongoose.model<MovieEntity>('Movie', MovieSchema, 'Movies');