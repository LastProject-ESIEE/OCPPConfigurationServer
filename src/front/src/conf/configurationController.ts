import { PageRequest } from "../pages/DisplayTable"
import { Firmware, TypeAllowed } from "./FirmwareController";

// Configuration type definition
export type Configuration = {
    id: number,
    name: string,
    description: string,
    lastEdit: Date,
    keyValues: string,
    firmware: Firmware
}

/**
 * Default value to tell the backend there is no selected configuration for a chargepoint
 */
export var noConfig: Configuration = {
    description: "",
    keyValues: "",
    firmware: {
        id: -1,
        version:"",
        url: "",
        constructor: "",
        typesAllowed: new Set<TypeAllowed>()
    },
    lastEdit: new Date(),
    id: -1,
    name: "Pas de configuration"
}

export type KeyValueConfiguration = {
    key: Transcriptor,
    value: string,
}

export type ErrorState = {
    name: string,
    firmware: string,
    description: string,
}

export type GlobalState = {
    name: string,
    description: string,
    configuration: KeyValueConfiguration[],
    firmware: string
}

export type Transcriptor = {
    id: number,
    fullName: string,
    regex: string,
}

export async function getTranscriptors(): Promise<Transcriptor[] | undefined> {

    let request = await fetch("/api/configuration/transcriptor")
    if(request.ok){
        let content = await request.json()
        let transcriptors = (content as Transcriptor[])
        if(transcriptors != null){
            console.log(transcriptors)
            return transcriptors
        }else{
            console.log("Fetch transcriptors failed " + content)
        }
    }else{
        console.log("Fetch transcriptors failed, error code:" +  request.status)
    }
    return undefined
}


export async function getAllConfigurations(): Promise<Configuration[] | undefined> {

    let request = await fetch(`/api/configuration/all`)
    if(request.ok){
        let content = await request.json()
        let configuration = (content as Configuration[])
        if(configuration != null){
            console.log(configuration)
            return configuration
        }else{
            console.log("Fetch configuration list failed " + content)
        }
    }else{
        console.log("Fetch configuration list failed, error code:" +  request.status)
    }
    return undefined
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