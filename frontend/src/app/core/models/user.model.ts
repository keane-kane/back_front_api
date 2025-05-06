export class User {
  constructor(
    public id: number,
    public firstName: string,
    public lastName: string,
    public username: string,
    public email: string,
    public roles: string[]
  ) {}

  get fullName(): string {
    return `${this.firstName} ${this.lastName}`;
  }

  get isAdmin(): boolean {
    return this.roles.includes('ROLE_ADMIN');
  }

  get isInstructor(): boolean {
    return this.roles.includes('ROLE_INSTRUCTOR');
  }

  get isStudent(): boolean {
    return this.roles.includes('ROLE_STUDENT');
  }

  get isAdministrative(): boolean {
    return this.roles.includes('ROLE_ADMINISTRATIVE');
  }
}
