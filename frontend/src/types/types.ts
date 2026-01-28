export type Role = "VOLUNTEER" | "PROMOTER";

export interface User {
  id?: number;
  email: string;
  role: Role;
  skills?: string;
  availability?: string;
}

export type OpportunityStatus = "OPEN" | "CLOSED";

export interface Opportunity {
  id: number;
  title: string;
  description: string;
  requiredSkills?: string;
  startDate: string;
  endDate: string;
  maxVolunteers: number;
  currentVolunteers: number;
  points: number;
  status: OpportunityStatus;
}

export interface Application {
  id: number;
  volunteer: User;
  opportunity: Opportunity;
  applicationDate: string;
  status: "PENDING" | "ACCEPTED" | "REJECTED";
}