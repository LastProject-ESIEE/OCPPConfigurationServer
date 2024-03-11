import { CreateFirmwareFormData } from "../pages/home/firmware/CreateFirmware"

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

export async function getFirmware(id: number): Promise<Firmware | undefined> {

    let request = await fetch(`/api/firmware/${id}`)
    if(request.ok){
        let content = await request.json()
        let firmware = content as Firmware
        if(firmware){
            return firmware
        }else{
            console.error("Failed to fetch firmware with id :" + id, content)
        }
    }else{
        console.error("Fetch firmware failed, error code:" +  request.status)
    }
    return undefined
}

export async function postCreateFirmware(firmware: CreateFirmwareFormData): Promise<boolean> {

    let typesArray: TypeAllowed[] = []
    firmware.typesAllowed.forEach(item => {
        typesArray.push(item)
    })

    let request = await fetch(window.location.origin + "/api/firmware/create",
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                version: firmware.version,
                url: firmware.url,
                constructor: firmware.constructor,
                typesAllowed: typesArray,
            })
        })
    if (request.ok) {
        return true
    } else {
        console.error("Create firmware failed, error code:" + request.status)
        return false
    }
}

export async function updateFirmware(id: number, firmware: CreateFirmwareFormData): Promise<boolean> {
    let typesArray: TypeAllowed[] = []
    firmware.typesAllowed.forEach(item => {
        typesArray.push(item)
    })

    let request = await fetch(`/api/firmware/update/${id}`,
        {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                version: firmware.version,
                url: firmware.url,
                constructor: firmware.constructor,
                typesAllowed: typesArray,
            })
        })
    if (request.ok) {
        return true
    } else {
        console.error("Couldn't update firmware, error code: " + request.status)
        return false
    }
}

export async function getTypeAllowed(): Promise<TypeAllowed[] | undefined> {

    let request = await fetch(`/api/type/all`)
    if(request.ok){
        let content = await request.json()
        let typesAllowed = content as TypeAllowed[]
        if(typesAllowed != null){
            return typesAllowed
        }else{
            console.log("Failed to fetch type allowed.")
        }
    }else{
        console.error("Fetch type allowed list failed, error code:" +  request.status)
    }
    return undefined
}