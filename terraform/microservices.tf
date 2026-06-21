# ==========================================
# 7. Servicios, Task Definitions y Target Groups
# ==========================================

# ----------------- Eureka Server -----------------
resource "aws_lb_target_group" "eurekaserver" {
  name        = "${var.project_name}-tg-eureka"
  port        = 8761
  protocol    = "HTTP"
  vpc_id      = data.aws_vpc.default.id
  target_type = "instance"

  health_check {
    path                = "/"
    healthy_threshold   = 2
    unhealthy_threshold = 2
    timeout             = 5
    interval            = 30
    matcher             = "200-499"
  }
}

resource "aws_ecs_task_definition" "eurekaserver" {
  family                   = "${var.project_name}-eurekaserver"
  requires_compatibilities = ["EC2"]
  network_mode             = "bridge"
  memory                   = "512"
  execution_role_arn       = data.aws_iam_role.lab_role.arn

  container_definitions = jsonencode([
    {
      name      = "eurekaserver"
      image     = "${aws_ecr_repository.repos["eurekaserver"].repository_url}:latest"
      essential = true
      memory    = 512
      portMappings = [{ containerPort = 8761, hostPort = 0 }]
      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.ecs_logs["eurekaserver"].name
          "awslogs-region"        = "us-east-1"
          "awslogs-stream-prefix" = "ecs"
        }
      }
    }
  ])
}

resource "aws_ecs_service" "eurekaserver" {
  name            = "${var.project_name}-eurekaserver-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.eurekaserver.arn
  desired_count   = 1

  deployment_minimum_healthy_percent = 50
  deployment_maximum_percent         = 200
  deployment_controller { type = "ECS" }

  capacity_provider_strategy {
    capacity_provider = aws_ecs_capacity_provider.ecs_cp.name
    weight            = 100
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.eurekaserver.arn
    container_name   = "eurekaserver"
    container_port   = 8761
  }

  lifecycle { ignore_changes = [desired_count] }
}

# ----------------- MS Coincidencias -----------------
resource "aws_ecs_task_definition" "ms_coincidencias" {
  family                   = "${var.project_name}-ms-coincidencias"
  requires_compatibilities = ["EC2"]
  network_mode             = "bridge"
  memory                   = "384"
  execution_role_arn       = data.aws_iam_role.lab_role.arn

  container_definitions = jsonencode([
    {
      name      = "ms-coincidencias"
      image     = "${aws_ecr_repository.repos["ms-coincidencias"].repository_url}:latest"
      essential = true
      memory    = 384
      environment = [
        { name = "EUREKA_CLIENT_SERVICEURL_DEFAULTZONE", value = "http://${aws_lb.main.dns_name}:8761/eureka/" },
        { name = "SPRING_DATASOURCE_URL", value = "jdbc:postgresql://${aws_instance.postgres_db.private_ip}:5432/dnf_db" },
        { name = "SPRING_DATASOURCE_USERNAME", value = "admin" }
      ]
      secrets = [
        { name = "SPRING_DATASOURCE_PASSWORD", valueFrom = aws_ssm_parameter.db_password.arn }
      ]
      portMappings = [{ containerPort = 8083, hostPort = 0 }]
      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.ecs_logs["ms-coincidencias"].name
          "awslogs-region"        = "us-east-1"
          "awslogs-stream-prefix" = "ecs"
        }
      }
    }
  ])
}

resource "aws_ecs_service" "ms_coincidencias" {
  name            = "${var.project_name}-ms-coincidencias-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.ms_coincidencias.arn
  desired_count   = 1

  deployment_minimum_healthy_percent = 50
  deployment_maximum_percent         = 200
  deployment_controller { type = "ECS" }

  capacity_provider_strategy {
    capacity_provider = aws_ecs_capacity_provider.ecs_cp.name
    weight            = 100
  }
  lifecycle { ignore_changes = [desired_count] }
}

# ----------------- MS Comunidad -----------------
resource "aws_ecs_task_definition" "ms_comunidad" {
  family                   = "${var.project_name}-ms-comunidad"
  requires_compatibilities = ["EC2"]
  network_mode             = "bridge"
  memory                   = "384"
  execution_role_arn       = data.aws_iam_role.lab_role.arn

  container_definitions = jsonencode([
    {
      name      = "ms-comunidad"
      image     = "${aws_ecr_repository.repos["ms-comunidad"].repository_url}:latest"
      essential = true
      memory    = 384
      environment = [
        { name = "EUREKA_CLIENT_SERVICEURL_DEFAULTZONE", value = "http://${aws_lb.main.dns_name}:8761/eureka/" },
        { name = "SPRING_DATASOURCE_URL", value = "jdbc:postgresql://${aws_instance.postgres_db.private_ip}:5432/dnf_db" },
        { name = "SPRING_DATASOURCE_USERNAME", value = "admin" }
      ]
      secrets = [
        { name = "SPRING_DATASOURCE_PASSWORD", valueFrom = aws_ssm_parameter.db_password.arn }
      ]
      portMappings = [{ containerPort = 8094, hostPort = 0 }]
      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.ecs_logs["ms-comunidad"].name
          "awslogs-region"        = "us-east-1"
          "awslogs-stream-prefix" = "ecs"
        }
      }
    }
  ])
}

resource "aws_ecs_service" "ms_comunidad" {
  name            = "${var.project_name}-ms-comunidad-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.ms_comunidad.arn
  desired_count   = 1

  deployment_minimum_healthy_percent = 50
  deployment_maximum_percent         = 200
  deployment_controller { type = "ECS" }

  capacity_provider_strategy {
    capacity_provider = aws_ecs_capacity_provider.ecs_cp.name
    weight            = 100
  }
  lifecycle { ignore_changes = [desired_count] }
}

# ----------------- MS Mascota -----------------
resource "aws_ecs_task_definition" "ms_mascota" {
  family                   = "${var.project_name}-ms-mascota"
  requires_compatibilities = ["EC2"]
  network_mode             = "bridge"
  memory                   = "384"
  execution_role_arn       = data.aws_iam_role.lab_role.arn

  container_definitions = jsonencode([
    {
      name      = "ms-mascota"
      image     = "${aws_ecr_repository.repos["ms-mascota"].repository_url}:latest"
      essential = true
      memory    = 384
      environment = [
        { name = "EUREKA_CLIENT_SERVICEURL_DEFAULTZONE", value = "http://${aws_lb.main.dns_name}:8761/eureka/" },
        { name = "SPRING_DATASOURCE_URL", value = "jdbc:postgresql://${aws_instance.postgres_db.private_ip}:5432/dnf_db" },
        { name = "SPRING_DATASOURCE_USERNAME", value = "admin" }
      ]
      secrets = [
        { name = "SPRING_DATASOURCE_PASSWORD", valueFrom = aws_ssm_parameter.db_password.arn }
      ]
      portMappings = [{ containerPort = 8081, hostPort = 0 }]
      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.ecs_logs["ms-mascota"].name
          "awslogs-region"        = "us-east-1"
          "awslogs-stream-prefix" = "ecs"
        }
      }
    }
  ])
}

resource "aws_ecs_service" "ms_mascota" {
  name            = "${var.project_name}-ms-mascota-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.ms_mascota.arn
  desired_count   = 1

  deployment_minimum_healthy_percent = 50
  deployment_maximum_percent         = 200
  deployment_controller { type = "ECS" }

  capacity_provider_strategy {
    capacity_provider = aws_ecs_capacity_provider.ecs_cp.name
    weight            = 100
  }
  lifecycle { ignore_changes = [desired_count] }
}

# ----------------- MS Notificaciones -----------------
resource "aws_ecs_task_definition" "ms_notificaciones" {
  family                   = "${var.project_name}-ms-notificaciones"
  requires_compatibilities = ["EC2"]
  network_mode             = "bridge"
  memory                   = "384"
  execution_role_arn       = data.aws_iam_role.lab_role.arn

  container_definitions = jsonencode([
    {
      name      = "ms-notificaciones"
      image     = "${aws_ecr_repository.repos["ms-notificaciones"].repository_url}:latest"
      essential = true
      memory    = 384
      environment = [
        { name = "EUREKA_CLIENT_SERVICEURL_DEFAULTZONE", value = "http://${aws_lb.main.dns_name}:8761/eureka/" },
        { name = "SPRING_DATASOURCE_URL", value = "jdbc:postgresql://${aws_instance.postgres_db.private_ip}:5432/dnf_db" },
        { name = "SPRING_DATASOURCE_USERNAME", value = "admin" }
      ]
      secrets = [
        { name = "SPRING_DATASOURCE_PASSWORD", valueFrom = aws_ssm_parameter.db_password.arn }
      ]
      portMappings = [{ containerPort = 8095, hostPort = 0 }]
      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.ecs_logs["ms-notificaciones"].name
          "awslogs-region"        = "us-east-1"
          "awslogs-stream-prefix" = "ecs"
        }
      }
    }
  ])
}

resource "aws_ecs_service" "ms_notificaciones" {
  name            = "${var.project_name}-ms-notificaciones-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.ms_notificaciones.arn
  desired_count   = 1

  deployment_minimum_healthy_percent = 50
  deployment_maximum_percent         = 200
  deployment_controller { type = "ECS" }

  capacity_provider_strategy {
    capacity_provider = aws_ecs_capacity_provider.ecs_cp.name
    weight            = 100
  }
  lifecycle { ignore_changes = [desired_count] }
}

# ----------------- MS Usuario -----------------
resource "aws_ecs_task_definition" "ms_usuario" {
  family                   = "${var.project_name}-ms-usuario"
  requires_compatibilities = ["EC2"]
  network_mode             = "bridge"
  memory                   = "384"
  execution_role_arn       = data.aws_iam_role.lab_role.arn

  container_definitions = jsonencode([
    {
      name      = "ms-usuario"
      image     = "${aws_ecr_repository.repos["ms-usuario"].repository_url}:latest"
      essential = true
      memory    = 384
      environment = [
        { name = "EUREKA_CLIENT_SERVICEURL_DEFAULTZONE", value = "http://${aws_lb.main.dns_name}:8761/eureka/" },
        { name = "SPRING_DATASOURCE_URL", value = "jdbc:postgresql://${aws_instance.postgres_db.private_ip}:5432/dnf_db" },
        { name = "SPRING_DATASOURCE_USERNAME", value = "admin" }
      ]
      secrets = [
        { name = "JWT_SECRET", valueFrom = aws_ssm_parameter.jwt_secret.arn },
        { name = "SPRING_DATASOURCE_PASSWORD", valueFrom = aws_ssm_parameter.db_password.arn }
      ]
      portMappings = [{ containerPort = 8082, hostPort = 0 }]
      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.ecs_logs["ms-usuario"].name
          "awslogs-region"        = "us-east-1"
          "awslogs-stream-prefix" = "ecs"
        }
      }
    }
  ])
}

resource "aws_ecs_service" "ms_usuario" {
  name            = "${var.project_name}-ms-usuario-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.ms_usuario.arn
  desired_count   = 1

  deployment_minimum_healthy_percent = 50
  deployment_maximum_percent         = 200
  deployment_controller { type = "ECS" }

  capacity_provider_strategy {
    capacity_provider = aws_ecs_capacity_provider.ecs_cp.name
    weight            = 100
  }
  lifecycle { ignore_changes = [desired_count] }
}

# ----------------- Frontend -----------------
resource "aws_lb_target_group" "frontend" {
  name        = "${var.project_name}-tg-frontend"
  port        = 80
  protocol    = "HTTP"
  vpc_id      = data.aws_vpc.default.id
  target_type = "instance"

  health_check {
    path                = "/"
    healthy_threshold   = 2
    unhealthy_threshold = 2
    timeout             = 5
    interval            = 30
    matcher             = "200-499"
  }
}

resource "aws_lb_listener_rule" "frontend_rule" {
  listener_arn = aws_lb_listener.http.arn
  priority     = 200
  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.frontend.arn
  }
  condition {
    path_pattern {
      values = ["/*"]
    }
  }
}

resource "aws_ecs_task_definition" "frontend" {
  family                   = "${var.project_name}-frontend"
  requires_compatibilities = ["EC2"]
  network_mode             = "bridge"
  memory                   = "256"
  cpu                      = "256"
  execution_role_arn       = data.aws_iam_role.lab_role.arn
  task_role_arn            = data.aws_iam_role.lab_role.arn

  container_definitions = jsonencode([
    {
      name      = "frontend"
      image     = "${aws_ecr_repository.repos["frontend"].repository_url}:latest"
      essential = true
      memory    = 256
      cpu       = 256
      environment = [
        { name = "VITE_SPRING_BOOT_API_URL", value = "http://${aws_lb.main.dns_name}" }
      ]
      portMappings = [{ containerPort = 80, hostPort = 0 }]
      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.ecs_logs["frontend"].name
          "awslogs-region"        = "us-east-1"
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

  deployment_minimum_healthy_percent = 50
  deployment_maximum_percent         = 200
  deployment_controller { type = "ECS" }

  capacity_provider_strategy {
    capacity_provider = aws_ecs_capacity_provider.ecs_cp.name
    weight            = 100
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.frontend.arn
    container_name   = "frontend"
    container_port   = 80
  }
  lifecycle { ignore_changes = [desired_count] }
}

# ----------------- API Gateway -----------------
resource "aws_lb_target_group" "api_gateway" {
  name        = "${var.project_name}-tg-api-gateway"
  port        = 8080
  protocol    = "HTTP"
  vpc_id      = data.aws_vpc.default.id
  target_type = "instance"

  health_check {
    path                = "/actuator/health"
    healthy_threshold   = 2
    unhealthy_threshold = 2
    timeout             = 5
    interval            = 30
    matcher             = "200-499"
  }
}

resource "aws_lb_listener_rule" "api_gateway_rule" {
  listener_arn = aws_lb_listener.http.arn
  priority     = 100
  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.api_gateway.arn
  }
  condition {
    path_pattern {
      values = ["/api/*"]
    }
  }
}

resource "aws_ecs_task_definition" "api_gateway" {
  family                   = "${var.project_name}-api-gateway"
  requires_compatibilities = ["EC2"]
  network_mode             = "bridge"
  memory                   = "384"
  execution_role_arn       = data.aws_iam_role.lab_role.arn

  container_definitions = jsonencode([
    {
      name      = "api-gateway"
      image     = "${aws_ecr_repository.repos["api-gateway"].repository_url}:latest"
      essential = true
      memory    = 384
      environment = [
        { name = "EUREKA_CLIENT_SERVICEURL_DEFAULTZONE", value = "http://${aws_lb.main.dns_name}:8761/eureka/" },
        { name = "SPRING_DATASOURCE_URL", value = "jdbc:postgresql://${aws_instance.postgres_db.private_ip}:5432/dnf_db" },
        { name = "SPRING_DATASOURCE_USERNAME", value = "admin" }
      ]
      secrets = [
        { name = "JWT_SECRET", valueFrom = aws_ssm_parameter.jwt_secret.arn },
        { name = "SPRING_DATASOURCE_PASSWORD", valueFrom = aws_ssm_parameter.db_password.arn }
      ]
      portMappings = [{ containerPort = 8080, hostPort = 0 }]
      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.ecs_logs["api-gateway"].name
          "awslogs-region"        = "us-east-1"
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

  deployment_minimum_healthy_percent = 50
  deployment_maximum_percent         = 200
  deployment_controller { type = "ECS" }

  capacity_provider_strategy {
    capacity_provider = aws_ecs_capacity_provider.ecs_cp.name
    weight            = 100
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.api_gateway.arn
    container_name   = "api-gateway"
    container_port   = 8080
  }
  lifecycle { ignore_changes = [desired_count] }
}
