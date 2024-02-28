import { PageRequest } from "../pages/DisplayTable"

// TechnicalLog type definition
export type TechnicalLog = {
    id: number,
    date: Date,
    component: 'BACKEND' | 'FRONTEND' | 'WEBSOCKET' | 'DATABASE',
    level: 'ALL' | 'DEBUG' | 'ERROR' | 'FATAL' | 'INFO' | 'OFF' | 'TRACE' | 'WARN',
    completeLog: string
}

export async function searchTechnicalLog(
    size: number = 10,
    page: number = 0,
    filter?: {filterField: string, filterValue: string },
    sort?: { sortField: string, sortOrder: 'asc' | 'desc' }): Promise<PageRequest<TechnicalLog> | undefined> {

    let request = await fetch(window.location.origin + `/api/log/technical/search?size=${size}&page=${page}`)
    if(request.ok){
        let content = await request.json()
        let technicalLog = (content as PageRequest<TechnicalLog>)
        if(technicalLog != null){
            console.log(technicalLog)
            return technicalLog
        }else{
            console.log("Fetch technical log page failed " + content)
        }
    }else{
        console.log("Fetch technical log list failed, error code:" +  request.status)
    }
    return undefined
}