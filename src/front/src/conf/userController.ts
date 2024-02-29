import { PageRequest } from "../pages/DisplayTable"

export enum Role { "VISUALIZER", "EDITOR", "ADMINISTRATOR" }

export type User = {
    id: number,
    mail: string,
    lastName: string,
    firstName: string,
    password: string,
    role: Role
}

export async function searchUser(
    size: number = 10,
    page: number = 0,
    filter?: {filterField: string, filterValue: string },
    sort?: { sortField: string, sortOrder: 'asc' | 'desc' }): Promise<PageRequest<User> | undefined> {

    let request = await fetch(window.location.origin + `/api/user/search?size=${size}&page=${page}`)
    if (request.ok) {
        let content = await request.json()
        let user = (content as PageRequest<User>)
        if (user != null) {
            console.log(user)
            return user
        } else {
            console.log("Fetch user page failed " + content)
        }
    } else {
        console.log("Fetch user list failed, error code:" +  request.status)
    }
    return undefined
}