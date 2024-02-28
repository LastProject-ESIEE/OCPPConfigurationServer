export type Configuration = {
    key: Key,
    value: string,
}

export type GlobalState = {
    name: string,
    description: string,
    configuration: Configuration[],
    firmware: string
}

export type Key = {
    id: string,
    keyName: string
}

export type ErrorState = {
    name: string,
    firmware: string,
    description: string,
}