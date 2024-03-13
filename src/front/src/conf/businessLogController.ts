import { ChargePoint } from "./chargePointController";

/**
 * Business log data model
 */
export type BusinessLog = {
    id: number,
    date: Date,
    user: User,
    chargepoint: ChargePoint,
    category: 'LOGIN' | 'STATUS' | 'FIRM' | 'CONFIG',
    level: 'ALL' | 'DEBUG' | 'ERROR' | 'FATAL' | 'INFO' | 'OFF' | 'TRACE' | 'WARN',
    completeLog: string
}

/**
 * User data model
 */
export type User = {
    id: number,
    firstName: string,
    lastName: string,
    email: string,
    role: 'VISUALIZER' | 'EDITOR' | 'ADMINISTRATOR'
}