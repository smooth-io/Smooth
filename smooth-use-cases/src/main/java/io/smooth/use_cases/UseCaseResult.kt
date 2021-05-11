package io.smooth.use_cases

import io.smooth.constraint.Constraint

sealed class UseCaseResult<Req, Res>

class Executing<Req, Res>(
    val req: Req
) : UseCaseResult<Req, Res>()

class Success<Req, Res>(
    val req: Req,
    val res: Res
) : UseCaseResult<Req, Res>()

class Failed<Req, Res>(
    val req: Req,
    val error: Throwable
) : UseCaseResult<Req, Res>()

class Progress<Req, Res>(
    val req: Req,
    val progress: Float,
    val metaData: Map<String, *>
) : UseCaseResult<Req, Res>()

class ConstraintsPending<Req, Res>(
    val req: Req
) : UseCaseResult<Req, Res>()


class ConstraintsNotMet<Req, Res>(
    val req: Req,
    val blockingConstraints: List<Constraint>
) : UseCaseResult<Req, Res>()
