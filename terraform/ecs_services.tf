resource "aws_cloudwatch_log_group" "ecs_logs" {
  for_each          = toset(local.services)
  name              = "/ecs/${var.project_name}-${each.key}"
  retention_in_days = 7
}

# Example Task Definition for Frontend
resource "aws_ecs_task_definition" "frontend" {
  family                   = "${var.project_name}-frontend"
  network_mode             = "bridge"
  requires_compatibilities = ["EC2"]
  execution_role_arn       = aws_iam_role.ecs_task_execution_role.arn
  task_role_arn            = aws_iam_role.ecs_task_execution_role.arn
  memory                   = "256"
  cpu                      = "256"

  container_definitions = jsonencode([
    {
      name      = "frontend"
      image     = "${aws_ecr_repository.repos["frontend"].repository_url}:latest"
      essential = true
      memory    = 256
      cpu       = 256
      portMappings = [
        {
          containerPort = 80
          hostPort      = 0 # Dynamic port mapping
          protocol      = "tcp"
        }
      ]
      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.ecs_logs["frontend"].name
          "awslogs-region"        = var.aws_region
          "awslogs-stream-prefix" = "ecs"
        }
      }
    }
  ])
}

resource "aws_ecs_service" "frontend" {
  name            = "${var.project_name}-frontend-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.frontend.arn
  desired_count   = 1

  capacity_provider_strategy {
    capacity_provider = aws_ecs_capacity_provider.ecs_cp.name
    weight            = 100
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.frontend.arn
    container_name   = "frontend"
    container_port   = 80
  }

  lifecycle {
    ignore_changes = [task_definition, desired_count]
  }

  # Ensure the ASG and ALB are created before the service
  depends_on = [aws_autoscaling_group.ecs_asg, aws_lb_listener.http]
}

# Example Task Definition for API Gateway
resource "aws_ecs_task_definition" "api_gateway" {
  family                   = "${var.project_name}-api-gateway"
  network_mode             = "bridge"
  requires_compatibilities = ["EC2"]
  execution_role_arn       = aws_iam_role.ecs_task_execution_role.arn
  memory                   = "384"

  container_definitions = jsonencode([
    {
      name      = "api-gateway"
      image     = "${aws_ecr_repository.repos["api-gateway"].repository_url}:latest"
      essential = true
      memory    = 384
      portMappings = [
        {
          containerPort = 8080
          hostPort      = 0
        }
      ]
      environment = [
        { name = "JWT_SECRET", value = "EstaEsUnaClaveSecretaMuyLargaParaAsegurarElTokenDeSanosYSalvos2026" },
        { name = "EUREKA_CLIENT_SERVICEURL_DEFAULTZONE", value = "http://eurekaserver:8761/eureka/" }
      ]
      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.ecs_logs["api-gateway"].name
          "awslogs-region"        = var.aws_region
          "awslogs-stream-prefix" = "ecs"
        }
      }
    }
  ])
}

resource "aws_ecs_service" "api_gateway" {
  name            = "${var.project_name}-api-gateway-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.api_gateway.arn
  desired_count   = 1

  capacity_provider_strategy {
    capacity_provider = aws_ecs_capacity_provider.ecs_cp.name
    weight            = 100
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.api_gateway.arn
    container_name   = "api-gateway"
    container_port   = 8080
  }

  lifecycle {
    ignore_changes = [task_definition, desired_count]
  }

  depends_on = [aws_autoscaling_group.ecs_asg, aws_lb_listener_rule.api_gateway_rule]
}

# Target Tracking Autoscaling for Frontend
resource "aws_appautoscaling_target" "frontend_target" {
  max_capacity       = 3
  min_capacity       = 1
  resource_id        = "service/${aws_ecs_cluster.main.name}/${aws_ecs_service.frontend.name}"
  scalable_dimension = "ecs:service:DesiredCount"
  service_namespace  = "ecs"
}

resource "aws_appautoscaling_policy" "frontend_cpu" {
  name               = "${var.project_name}-frontend-cpu"
  policy_type        = "TargetTrackingScaling"
  resource_id        = aws_appautoscaling_target.frontend_target.resource_id
  scalable_dimension = aws_appautoscaling_target.frontend_target.scalable_dimension
  service_namespace  = aws_appautoscaling_target.frontend_target.service_namespace

  target_tracking_scaling_policy_configuration {
    predefined_metric_specification {
      predefined_metric_type = "ECSServiceAverageCPUUtilization"
    }
    target_value       = 50.0
    scale_in_cooldown  = 300
    scale_out_cooldown = 300
  }
}
