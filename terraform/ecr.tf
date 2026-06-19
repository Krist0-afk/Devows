locals {
  services = [
    "frontend",
    "api-gateway",
    "eurekaserver",
    "ms-coincidencias",
    "ms-comunidad",
    "ms-mascota",
    "ms-notificaciones",
    "ms-usuario"
  ]
}

resource "aws_ecr_repository" "repos" {
  for_each             = toset(local.services)
  name                 = "${var.project_name}-${each.key}"
  image_tag_mutability = "MUTABLE"

  image_scanning_configuration {
    scan_on_push = false # Disabled to save costs/time, enable if required
  }
}
