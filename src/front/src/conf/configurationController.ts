import { PageRequest } from "../pages/DisplayTable"
import { Firmware } from "./FirmwareController";

// Configuration type definition
export type Configuration = {
    id: number,
    name: string,
    description: string,
    lastEdit: Date,
    configuration: string,
    firmware: Firmware
}

export type ConfigurationEntry = {
    key: Key,
    value: string,
}

export type GlobalState = {
    name: string,
    description: string,
    configuration: ConfigurationEntry[],
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


export async function searchConfiguration(
    size: number = 10,
    page: number = 0,
    filter?: {filterField: string, filterValue: string },
    sort?: { sortField: string, sortOrder: 'asc' | 'desc' }): Promise<PageRequest<Configuration> | undefined> {

    let request = await fetch(window.location.origin + `/api/configuration/search?size=${size}&page=${page}`)
    if(request.ok){
        let content = await request.json()
        let configuration = (content as PageRequest<Configuration>)
        if(configuration != null){
            console.log(configuration)
            return configuration
        }else{
            console.log("Fetch charge point page failed " + content)
        }
    }else{
        console.log("Fetch configuration list failed, error code:" +  request.status)
    }
    return undefined
}

export async function postNewConfiguration(configuration: GlobalState): Promise<boolean> {
    let myConfig = configuration.configuration.map(keyValue => `"${keyValue.key.id}":"${keyValue.value}"`)
        .join(", ")

    myConfig = "{" + myConfig + "}"

    console.log(JSON.parse(myConfig))

    let request = await fetch(window.location.origin + "/api/configuration/create",
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                name: configuration.name,
                description: configuration.description,
                configuration: myConfig,
                firmware: configuration.firmware
            })
        })
    if (request.ok) {
        return true
    } else {
        console.log("Fetch configuration list failed, error code:" + request.status)
        return false
    }
}

export async function getConfiguration(id: number): Promise<Configuration | undefined> {

    let request = await fetch(window.location.origin + `/api/configuration/${id}`)
    if(request.ok){
        let content = await request.json()
        let configuration = JSON.parse(content) as Configuration
        if(configuration != null){
            console.log(configuration)
            return configuration
        }else{
            console.log("Failed to fetch configuration with id :" + id, content)
        }
    }else{
        console.log("Fetch configuration list failed, error code:" +  request.status)
    }
    return undefined
}