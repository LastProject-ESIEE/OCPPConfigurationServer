import { PageRequest } from "../pages/DisplayTable"
import { ChargePoint } from "./chargePointController";
import internal from "stream";

export type BusinessLog = {
    id: number,
    date: Date,
    user: User,
    chargepoint: ChargePoint,
    category: 'LOGIN' | 'STATUS' | 'FIRM' | 'CONFIG',
    level: 'ALL' | 'DEBUG' | 'ERROR' | 'FATAL' | 'INFO' | 'OFF' | 'TRACE' | 'WARN',
    completeLog: string
}

export type User = {
    id: number,
    firstName: string,
    lastName: string,
    email: string,
    role: 'VISUALIZER' | 'EDITOR' | 'ADMINISTRATOR'
}

export async function searchBusinessLog(
    size: number = 10,
    page: number = 0,
    filter?: {filterField: string, filterValue: string },
    sort?: { sortField: string, sortOrder: 'asc' | 'desc' }): Promise<PageRequest<BusinessLog> | undefined> {

    let request = await fetch(window.location.origin + `/api/log/business/search?size=${size}&page=${page}`)
    if(request.ok){
        let content = await request.json()
        let businessLog = (content as PageRequest<BusinessLog>)
        if(businessLog != null){
            console.log(businessLog)
            return businessLog
        }else{
            console.log("Fetch business log page failed " + content)
        }
    }else{
        console.log("Fetch business log failed, error code:" +  request.status)
    }
    return undefined
}
