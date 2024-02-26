export type Configuration = {
    key: string,
    value: string,
}

export type GlobalState = {
    name: string,
    description: string,
    configuration: Configuration[],
    firmware: string
}