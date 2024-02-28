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