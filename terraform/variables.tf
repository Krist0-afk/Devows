variable "aws_region" {
  default = "us-east-1"
}

variable "project_name" {
  default = "devows-project"
}

variable "instance_type" {
  description = "EC2 instance type for ECS (budget constraints)"
  default     = "t3.micro"
}
