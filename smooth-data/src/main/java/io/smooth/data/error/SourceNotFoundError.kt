package io.smooth.data.error


class SourceNotFoundError(val sourceName: String) : DataError("Data Source not found")