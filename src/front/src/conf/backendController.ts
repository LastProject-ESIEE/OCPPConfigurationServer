import { PageRequest } from "../sharedComponents/DisplayTable"

export type SearchFilter = {filterField: string, filterValue: string }

export type SearchElementsParameters = {
    size: number,
    page: number,
    filters?: SearchFilter[],
    sort?: { sortField: string, sortOrder: 'asc' | 'desc' }
}

// Fetch elements from backend using pagination, filters and sorting
export async function searchElements<T>(path: string, params?: SearchElementsParameters ): Promise<PageRequest<T> | undefined> {
    let formattedRequest = path
    if(params){
        formattedRequest += `?size=${params.size}`
        formattedRequest += `&page=${params.page}`
        if(params.filters){
            formattedRequest += "&request=" + encodeURI(params.filters.map(filter => {
                return `${filter.filterField}:\`${filter.filterValue}\``
            }).join(","))
        }
    }
    let request = await fetch(formattedRequest)
    if(request.ok){
        let content = await request.json()
        let response = (content as PageRequest<T>)
        if(response){
            console.log(response)
            return response
        }else{
            console.log("Fetch page failed " + content)
        }
    }else{
        console.log("Fetch list failed, error code:" +  request.status)
    }
    return undefined
}

// Fetch all elements from the backend
export async function getAllElements<T>(path: string): Promise<T[] | undefined> {
    let request = await fetch(path)
    if(request.ok){
        let content = await request.json()
        let elements = content as T[]
        if(elements){
            return elements
        }else{
            console.error("Failed to fetch elements :", content)
        }
    }else{
        console.error("Fetch elements failed, error code:" +  request.status)
    }
    return undefined
}

// Fetch an element by id
export async function getElementById<T>(path: string, id: number) {
    let request = await fetch(`${path}/${id}`)
    if(request.ok){
        let content = await request.json()
        let element = (content as T)
        if(element != null){
            return element
        }else{
            console.error("Fetch element failed " + content)
        }
    }else{
        console.error("Fetch element failed, error code:" +  request.status)
    }
    return undefined
}
