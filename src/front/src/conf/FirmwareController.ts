import { CreateFirmwareFormData } from "../pages/home/firmware/CreateFirmware"
import { PageRequest } from "../sharedComponents/DisplayTable"

// TypeAllowed type definition
export type TypeAllowed = {
    id: number,
    constructor: string,
    type: string
}

// Firmware type definition
export type Firmware = {
    id: number,
    url: string,
    version: string,
    constructor: string,
    typesAllowed: Set<TypeAllowed>
}



export async function searchFirmware(
    size: number = 10,
    page: number = 0,
    filter?: { filterField: string, filterValue: string },
    sort?: { sortField: string, sortOrder: 'asc' | 'desc' }): Promise<PageRequest<Firmware> | undefined> {

    let request = await fetch(window.location.origin + `/api/firmware/search?size=${size}&page=${page}`)
    if (request.ok) {
        let content = await request.json()
        let firmware = (content as PageRequest<Firmware>)
        if (firmware != null) {
            console.log("firmware", firmware)
            return firmware
        } else {
            console.log("Fetch firmware page failed " + content)
        }
    } else {
        console.log("Fetch firmware page failed, error code:" + request.status)
    }
    return undefined
}

export async function postCreateFirmware(firmware: CreateFirmwareFormData): Promise<boolean> {

    console.log(JSON.stringify(firmware))

    let request = await fetch(window.location.origin + "/api/firmware/create",
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                version: firmware.version,
                url: firmware.url,
                constructeur: firmware.constructeur,
            })
        })
    if (request.ok) {
        return true
    } else {
        console.log("Create firmware failed, error code:" + request.status)
        return false
    }
}

export async function getTypeAllowed(): Promise<TypeAllowed[] | undefined> {

    let request = await fetch(window.location.origin + `/api/type/all`)
    if(request.ok){
        let content = await request.json()
        let typesAllowed = content as TypeAllowed[]
        if(typesAllowed != null){
            console.log(typesAllowed)
            return typesAllowed
        }else{
            console.log("Failed to fetch type allowed.")
        }
    }else{
        console.log("Fetch type allowed list failed, error code:" +  request.status)
    }
    return undefined
}