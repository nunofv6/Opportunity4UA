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
  status: "PENDING" | "ACCEPTED" | "REJECTED" | "COMPLETED";
}

export type CreateApplication = {
  id: number;
  status: string;
  appliedAt: string;
  opportunityId: number;
  opportunityTitle: string;
};

export interface Reward {
  id: number;
  volunteer: User;
  opportunity: Opportunity;
  points: number;
  awardedAt: string;
}

export interface RewardItem {
  id: number;
  name: string;
  description: string;
  costPoints: number;
  active: boolean;
}